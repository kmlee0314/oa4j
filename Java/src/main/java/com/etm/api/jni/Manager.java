/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.jni;

import com.etm.api.base.JDpVCItem;
import com.etm.api.base.JHotLinkWaitForAnswer;
import com.etm.api.var.DpIdentifierVar;
import com.etm.api.var.DynVar;
import com.etm.api.var.LangTextVar;
import com.etm.api.var.TimeVar;
import com.etm.api.var.Variable;

/**
 *
 * @author vogler
 */
public abstract class Manager {
    public native String apiGetLogPath();
    public native String apiGetDataPath();
    
    public int apiStartup(int manType, String[] argv) { return apiStartup(manType, argv, true, true); }
    public native int apiStartup(int manType, String[] argv, boolean connectToData, boolean connectToEvent);
    public native int apiShutdown();
    
    public native void apiDispatch(int sec, int usec);    
    
    public native int apiDpGet(JHotLinkWaitForAnswer hdl, DpIdentifierVar[] dps);
    public native int apiDpSet(JHotLinkWaitForAnswer hdl, JDpVCItem[] dps);
    public native int apiDpSetTimed(JHotLinkWaitForAnswer hdl, TimeVar originTime, JDpVCItem[] dps);
    public native int apiDpQuery(JHotLinkWaitForAnswer hdl, String query);
    
    public native int apiDpConnect(JHotLinkWaitForAnswer hdl, String dp);
    public native int apiDpDisconnect(JHotLinkWaitForAnswer hdl, String dp);
    
    public native int apiDpConnectArray(JHotLinkWaitForAnswer hdl, String[] dps);    
    public native int apiDpDisconnectArray(JHotLinkWaitForAnswer hdl, String[] dps);    
    
    public native int apiDpQueryConnectSingle(JHotLinkWaitForAnswer hdl, boolean values, String query);
    public native int apiDpQueryConnectAll(JHotLinkWaitForAnswer hdl, boolean values, String query);
    public native int apiDpQueryDisonnect(JHotLinkWaitForAnswer hdl);    
    
    public native int apiAlertConnect(JHotLinkWaitForAnswer hdl, String[] dps);
    public native int apiAlertDisconnect(JHotLinkWaitForAnswer hdl, String[] dps);
    
    public native String[] apiGetIdSet(String pattern);    
    public native String[] apiGetIdSetOfType(String pattern, String type);    
    
    public native LangTextVar apiDpGetComment(DpIdentifierVar dp);
    
    public native void apiDoReceiveSysMsg(long cPtrSysMsg);
    public native void apiDoReceiveDpMsg(long cPtrDpMsg);
    
    public native int apiSendArchivedDPs(DynVar elements, boolean isAlert);  
    
    private native void apiSetManagerState(int state);
    public void apiSetManagerState(ManagerState state) { apiSetManagerState(state.value); }
       
    // callbacks from API   
    public abstract boolean doReceiveSysMsg(long cPtrSysMsg);    
    public abstract boolean doReceiveDpMsg(long cPtrDpMsg);
    
    public int callbackAnswer(int id, int idx) {    
        return callbackAnswer(id, idx, null, null);
    }
    abstract public int callbackAnswer(int id, int idx, DpIdentifierVar dpid, Variable var);    
    
    public int callbackHotlink(int id, int idx) {
        return callbackHotlink(id, idx, null, null);
    }
    abstract public int callbackHotlink(int id, int idx, DpIdentifierVar dpid, Variable var);  
    
    public native int apiProcessHotlinkGroup(int id, long ptrDpHlGroup);
    public abstract int callbackHotlinkGroup(int id, long ptrDpHlGroup);
}
