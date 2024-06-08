package org.ecsail.mvci_slip;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.widgetfx.VBoxFx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SlipView implements Builder<Region> {
    private final SlipModel slipModel;
    Consumer<SlipMessage> action;

    public SlipView(SlipModel slipModel, Consumer<SlipMessage> m) {
        this.slipModel = slipModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
        slipModel.setMainBox(hBox);
        slipModel.setBorderPane(borderPane);
        setListsLoadedListener();
        return borderPane;
    }

    private void setListsLoadedListener() {
        slipModel.listsLoadedProperty().addListener((obs, wasLoaded, isNowLoaded) -> {
            if (isNowLoaded) {
                buildDocks();
                setSizeListener();
            }
        });
    }

    private void setSizeListener() {
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> Platform.runLater(this::buildDocks);
        slipModel.getBorderPane().widthProperty().addListener(sizeListener);
        slipModel.getBorderPane().heightProperty().addListener(sizeListener);
    }

    private void buildDocks() {
        slipModel.getMainBox().getChildren().clear();
        // get space for outside of docks, between window edge and docks
        double insets = (slipModel.getMainBox().getWidth() * .02) / 2;
        slipModel.getMainBox().setPadding(new Insets(10, insets, 0, insets));
        // this is the width of each dock
        double dockWidth = (slipModel.getMainBox().getWidth() - (insets * 2)) / 4;
        // sets space between docks
        slipModel.setDockPadding(dockWidth * 0.03); // changing this to .01 will fix the border issue
        slipModel.getMainBox().setSpacing(slipModel.getDockPadding());
        // dock width adjusted for padding
        slipModel.setDockWidth((dockWidth - slipModel.getDockPadding()) * 1.00773); // 1.00773 corrects for error
        // dock height (-10 is for top inset)
        slipModel.setDockHeight((slipModel.getMainBox().getHeight() / 18) - 10);
        // sets spacing between text on each dock
        slipModel.setDockTextSpacing(slipModel.getDockHeight() * .1);

        slipModel.getMainBox().getChildren().addAll(
                createDock("A", "F"),
                createDock("B", ""),
                createDock("C", ""),
                createDock("D", ""));
    }

    private Node createDock(String dockA, String dockB) {
        VBox vBox = new VBox();
        HBox.setHgrow(vBox, Priority.ALWAYS);
        filterAndSortDocks(dockA);
        if (!dockB.equals("")) {
            renderDockSections(vBox, true);
            filterAndSortDocks(dockB);
            renderDockSections(vBox, false);
        } else {
            renderDockSections(vBox, false);
        }
        return vBox;
    }

    private void renderDockSections(VBox vBox, boolean needsSpacer) {
        vBox.getChildren().add(buildDockSpacer("top-cap"));
        boolean leftDockVisible;
        for (int i = 0; i < slipModel.getTempList().size(); i++) {
            leftDockVisible = true;
            SlipStructureDTO section = slipModel.getTempList().get(i);
            if (section.getSlip4().equals("none") && section.getSlip4().equals("none")) leftDockVisible = false;
            vBox.getChildren().add(buildDockSection(leftDockVisible, section.getSlip4(), section.getSlip3(), section.getSlip2(), section.getSlip1()));
            if (i < slipModel.getTempList().size() - 1) {
                vBox.getChildren().add(buildDockSpacer("segment"));
            }
        }
        vBox.getChildren().add(buildDockSpacer("bottom-cap"));
        if (needsSpacer)
            vBox.getChildren().add(buildDockSpacer("spacer"));
    }

    private Node buildDockSection(boolean leftDockVisible, String mem1, String mem2, String mem3, String mem4) {
        HBox hBox = new HBox();
        double dockSection = slipModel.getDockWidth() * 0.45;
        hBox.setPrefHeight(slipModel.getDockHeight());
        hBox.setMaxHeight(slipModel.getDockHeight());
        hBox.getChildren().add(buildDock(dockSection, true, leftDockVisible, mem1, mem2));
        hBox.getChildren().add(buildWalkWay(leftDockVisible));
        hBox.getChildren().add(buildDock(dockSection, false, leftDockVisible, mem3, mem4));
        return hBox;
    }

    private Node buildDockSpacer(String type) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = VBoxFx.vBoxOf(slipModel.getDockWidth() * 0.1, slipModel.getDockHeight() * 0.3);
        switch (type) {
            case "top-cap" ->
                    vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 1px 1px 0px 1px;");
            case "segment" ->
                    vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 0px 1px 0px 1px;");
            case "bottom-cap" ->
                    vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 0px 1px 1px 1px;");
        }
        hBox.getChildren().add(vBox);
        return hBox;
    }

    private Node buildWalkWay(boolean leftDockVisible) {
        VBox vBox = VBoxFx.vBoxOf(slipModel.getDockWidth() * 0.1, slipModel.getDockHeight());
        if (leftDockVisible)
            vBox.setStyle("-fx-background-color: white;");
        else
            vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 0px 0px 0px 1px;");
        return vBox;
    }

    private Node buildDock(double dockWidth, boolean isLeftDock, boolean isVisible, String mem1, String mem2) {
        VBox vBox = VBoxFx.vBoxOf(dockWidth, slipModel.getDockHeight());
        vBox.setSpacing(slipModel.getDockSpacing());
        if (isLeftDock) {
            if (isVisible) {
                vBox.setAlignment(Pos.CENTER_RIGHT);
                vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 1px 0px 1px 1px;");
                vBox.getChildren().addAll(getName(mem1, true), getName(mem2, true));
            }
        } else {
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setStyle("-fx-background-color: white; -fx-border-color: #1658e7; -fx-border-width: 1px 1px 1px 0px;");
            vBox.getChildren().addAll(getName(mem1, false), getName(mem2, false));
        }
        return vBox;
    }

    private Node getName(String mem, boolean isLeftDock) {
        Text text = new Text();
        if (mem.equals("none")) {
            text.setText("");
        } else {
            SlipInfoDTO slip = findSlipInfoByNumber(mem);
            String lastName = getLastName(slip);

            if(slipModel.getMainBox().getWidth() > 1644) {
                String firstName = getFirstName(slip);
                if (isLeftDock) {
                    text.setText(firstName + " " + lastName + " " + slip.getSlipNumber());
                } else {
                    text.setText(slip.getSlipNumber() + " " + firstName + " " + lastName);
                }
            } else {
                if (isLeftDock) {
                    text.setText(lastName + " " + slip.getSlipNumber());
                } else {
                    text.setText(slip.getSlipNumber() + " " + lastName);
                }
            }
            setMouseListener(text, slip);
        }
        return text;
    }

    private void setMouseListener(Text text, SlipInfoDTO slip) {
        Tooltip tooltip = new Tooltip(getTooltipText(slip));
        tooltip.setShowDelay(javafx.util.Duration.millis(100));
        tooltip.setHideDelay(javafx.util.Duration.millis(100));
        tooltip.setShowDuration(javafx.util.Duration.seconds(10));
        Tooltip.install(text, tooltip);

        if (slip.getSubleaserMsid() != 0) {  // blue if it is a sublease
            text.setFill(Color.CORNFLOWERBLUE);
            text.setOnMouseExited(ex -> text.setFill(Color.CORNFLOWERBLUE));
        } else {
            text.setOnMouseExited(ex -> text.setFill(Color.BLACK));
        }

        text.setOnMouseEntered(en -> text.setFill(Color.RED));

        text.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (slip.getSubleaserMsid() != 0) {
                    slipModel.setSelectedMsId(slip.getSubleaserMsid());
                } else {
                    slipModel.setSelectedMsId(slip.getOwnerMsid());
                }
                Integer selectedMsId = slipModel.getSelectedMsId();
                // prevents unnecessary exceptions when you click on a racing or 48-hour dock
                if (selectedMsId != null && selectedMsId != 0) {
                    action.accept(SlipMessage.LAUNCH_TAB);
                }
            }
        });
    }

    private String getTooltipText(SlipInfoDTO slip) {
        StringBuilder tooltipText = new StringBuilder();
        tooltipText.append("Owner: ").append(slip.getOwnerFirstName()).append(" ").append(slip.getOwnerLastName()).append("\n");
        tooltipText.append("Slip Number: ").append(slip.getSlipNumber()).append("\n");
        if (slip.getSubleaserMsid() != 0) {
            tooltipText.append("Subleaser: ").append(slip.getSubleaserFirstName()).append(" ").append(slip.getSubleaserLastName());
        }
        return tooltipText.toString();
    }

    private String getFirstName(SlipInfoDTO slip) {
        String firstName = "";
        if (slip.getAltText() == null) {
            if(slip.getSubleaserFirstName() != null) firstName = slip.getSubleaserFirstName();
            else firstName = slip.getOwnerFirstName();
        }
            return firstName;
    }

    private static String getLastName(SlipInfoDTO slip) {
        String lastName;
        if (slip.getAltText() != null) lastName = slip.getAltText();
        else if (slip.getSubleaserLastName() != null) {
            lastName = slip.getSubleaserLastName();
        } else lastName = slip.getOwnerLastName();
        return lastName;
    }

    public void filterAndSortDocks(String dockLetter) {
        slipModel.getTempList().clear();
        slipModel.setTempList(slipModel.getSlipStructureDTOS().stream()
                .filter(dock -> dockLetter.equals(dock.getDock()))
                .sorted(Comparator.comparingInt(SlipStructureDTO::getDockSection))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    public SlipInfoDTO findSlipInfoByNumber(String slipNumber) {
        Optional<SlipInfoDTO> slipInfo = slipModel.getSlipInfoDTOS().stream()
                .filter(slip -> slipNumber.equals(slip.getSlipNumber()))
                .findFirst();
        return slipInfo.orElse(null);
    }
}
