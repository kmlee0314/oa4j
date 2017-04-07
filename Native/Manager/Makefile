
include $(API_ROOT)/Manager.mk

INCLUDE = $(MANAGER_INCL) -I. -I$(JDK_HOME)/include -I$(JDK_HOME)/include/linux

LIBS = $(MANAGER_LIBS) $(LINKLIB) $(JDK_HOME)/jre/lib/amd64/server/libjvm.so

OBJS = $(MANAGER_OBJS)

MYOBJS = WCCOAJavaMain.o \
         WCCOAJavaManager.o \
         WCCOAJavaResources.o \
         ../LibJava/Java.o \
         at_rocworks_oa4j_jni_AnswerGroup.o \
         at_rocworks_oa4j_jni_DpIdValueList.o \
         at_rocworks_oa4j_jni_DpMsgAnswer.o \
         at_rocworks_oa4j_jni_DpMsgComplexVC.o \
         at_rocworks_oa4j_jni_DpMsgHotLink.o \
         at_rocworks_oa4j_jni_DpMsgRequest.o \
         at_rocworks_oa4j_jni_DpMsgValueChange.o \
         at_rocworks_oa4j_jni_DpVCGroup.o \
         at_rocworks_oa4j_jni_DpVCItem.o \
         at_rocworks_oa4j_jni_Manager.o \
         at_rocworks_oa4j_jni_Msg.o \
         at_rocworks_oa4j_jni_RequestGroup.o \
         at_rocworks_oa4j_jni_RequestItem.o \
         at_rocworks_oa4j_jni_SysMsg.o \
         WCCOAJavaAlertHotLinkWaitForAnswer.o \
         WCCOAJavaHotLinkWaitForAnswer.o 

PVSS00WCCOAJava: $(MYOBJS)
	@rm -f addVerInfo.o
	@$(MAKE) addVerInfo.o
	mv Java.o ../LibJava
	$(LINK_CMD) -o WCCOAjava addVerInfo.o $(MYOBJS) $(OBJS) $(LIBS)
	gcc -shared -gcc-11 -o libWCCOAjava.so ../LibJava/Java.o *.o $(LIBS)

clean:
	@rm -f *.o WCCOAjava WCCOAjava.so ../LibJava/Java.o

addVerInfo.cxx: $(API_ROOT)/addVerInfo.cxx
	@cp -f $(API_ROOT)/addVerInfo.cxx addVerInfo.cxx

addVerInfo.o: $(OFILES) addVerInfo.cxx
    
