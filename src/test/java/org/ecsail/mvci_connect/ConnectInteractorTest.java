package org.ecsail.mvci_connect;

import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.ObjectFx;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class ConnectInteractorTest implements ConfigFilePaths {

    @InjectMocks
    private ConnectInteractor connectInteractor;

    @Spy
    private FileIO fileIO;

    @Mock
    private ObjectFx objectFx;

//    @Test
//    public void testSupplyLogins_HostFileExists() {
//        // Set up test scenario where host file exists
//        when(fileIO.hostFileExists(ConfigFilePaths.LOGIN_FILE)).thenReturn(true);
//
//
//        List<LoginDTO> result = connectInteractor.supplyLogins();
//
//        // Verify that openLoginObjects was called
//        verify(connectInteractor, times(1)).openLoginObjects(result);
//
//        // Additional assertions if necessary
//    }
}
