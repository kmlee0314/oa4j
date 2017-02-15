
include $(API_ROOT)/Manager.mk

INCLUDE = $(MANAGER_INCL) -I. -I$(JDK_HOME)/include -I$(JDK_HOME)/include/linux

LIBS = $(MANAGER_LIBS) $(LINKLIB) $(JDK_HOME)/jre/lib/amd64/server/libjvm.so

OBJS = $(MANAGER_OBJS)

MYOBJS = WCCOAJavaMain.o \
         WCCOAJavaManager.o \
         WCCOAJavaResources.o \
         ../LibJava/Java.o \
         com_etm_api_jni_AnswerGroup.o \
         com_etm_api_jni_DpIdValueList.o \
         com_etm_api_jni_DpMsgAnswer.o \
         com_etm_api_jni_DpMsgComplexVC.o \
         com_etm_api_jni_DpMsgHotLink.o \
         com_etm_api_jni_DpMsgRequest.o \
         com_etm_api_jni_DpMsgValueChange.o \
         com_etm_api_jni_DpVCGroup.o \
         com_etm_api_jni_DpVCItem.o \
         com_etm_api_jni_Manager.o \
         com_etm_api_jni_Msg.o \
         com_etm_api_jni_RequestGroup.o \
         com_etm_api_jni_RequestItem.o \
         com_etm_api_jni_SysMsg.o \
         com_etm_nosql_logger_LoggerManager.o \
         WCCOAJavaAlertHotLinkWaitForAnswer.o \
         WCCOAJavaHotLinkWaitForAnswer.o \
         WCCOAJavaHotLinkWaitForAnswerLOGGER.o

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
    
