package mdl.sinlov.pluginapk;


public final class MDLSDK {
    private static MDLSDK instance;

    public static MDLSDK getInstance() {
        if (null == instance) {
            MDLSDK.instance = new MDLSDK();
        }
        return instance;
    }

    private MDLSDK() {

    }
}
