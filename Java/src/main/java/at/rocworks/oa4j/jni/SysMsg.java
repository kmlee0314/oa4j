/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oa4j.jni;

/**
 *
 * @author vogler
 */
public class SysMsg extends Msg {
    public SysMsg() {
        super();
    }
    
    public SysMsg(long cptr) {
        super(cptr);
    }   
    
    private native int getSysMsgType(long cptr);   

    public SysMsgType getSysMsgType() {
        return SysMsgType.values()[getSysMsgType(cptr)];
    }
}
