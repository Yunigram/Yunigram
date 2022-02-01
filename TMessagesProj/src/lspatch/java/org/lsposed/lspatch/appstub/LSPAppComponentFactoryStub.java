package org.lsposed.lspatch.appstub;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.core.app.CoreComponentFactory;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Objects;

@SuppressLint({"UnsafeDynamicallyLoadedCode", "RestrictedApi"})
public class LSPAppComponentFactoryStub extends CoreComponentFactory {
    public static byte[] dex = null;

    static {
        var cl = Objects.requireNonNull(LSPAppComponentFactoryStub.class.getClassLoader());
        try (var is = cl.getResourceAsStream("assets/org.lsposed.lspatch.lspatch/lsp.dex");
             var os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int n;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
            }
            dex = os.toByteArray();
        } catch (Throwable e) {
            Log.e("LSPatch", "load dex error", e);
        }

        try {
            Class<?> VMRuntime = Class.forName("dalvik.system.VMRuntime");
            Method getRuntime = VMRuntime.getDeclaredMethod("getRuntime");
            getRuntime.setAccessible(true);
            Method vmInstructionSet = VMRuntime.getDeclaredMethod("vmInstructionSet");
            vmInstructionSet.setAccessible(true);

            String arch = (String) vmInstructionSet.invoke(getRuntime.invoke(null));
            String path = cl.getResource("assets/org.lsposed.lspatch.lspatch/lspd/" + arch + "/liblspd.so").getPath().substring(5);
            System.load(path);
        } catch (Throwable e) {
            Log.e("LSPatch", "load lspd error", e);
        }
    }
}