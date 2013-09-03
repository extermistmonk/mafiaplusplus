package com.sc2mafia.mafiaplusplus;

import org.mozilla.javascript.*;

public class SandboxedContextFactory extends ContextFactory {

    @SuppressWarnings("deprecation")
    static class SecureContext extends Context {
	long startTime;
    }

    @Override
    protected Context makeContext()
    {
        SecureContext cx = new SecureContext();
        cx.setOptimizationLevel(-1);
        cx.setInstructionObserverThreshold(10000);
        return cx;
    } 

    protected void observeInstructionCount(Context cx, int instructionCount) {
	SecureContext mcx = (SecureContext) cx;
	long currentTime = System.currentTimeMillis();
	if (currentTime - mcx.startTime > 5 * 1000) {
	    throw new Error();
	}
    }

    protected Object doTopCall(Callable callable, Context cx, Scriptable scope,
	    Scriptable thisObj, Object[] args) {
	SecureContext mcx = (SecureContext) cx;
	mcx.startTime = System.currentTimeMillis();

	return super.doTopCall(callable, cx, scope, thisObj, args);
    }
}

class SandboxedNativeJavaObject extends NativeJavaObject {
    private static final long serialVersionUID = -9172561421760308930L;

    public SandboxedNativeJavaObject(Scriptable scope, Object javaObject,
	    Class<?> staticType) {
	super(scope, javaObject, staticType);
    }

    @Override
    public Object get(String name, Scriptable start) {
	if (name.equals("getClass")) {
	    return NOT_FOUND;
	}

	return super.get(name, start);
    }
}

class SandboxedWrapFactory extends WrapFactory {
    @Override
    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope,
	    Object javaObject, Class<?> staticType) {
	return new SandboxedNativeJavaObject(scope, javaObject, staticType);
    }
}
