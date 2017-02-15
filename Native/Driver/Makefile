
include $(API_ROOT)/ComDrv.mk

INCLUDE = $(COMDRV_INCL) -I. -I$(JDK_HOME)/include -I$(JDK_HOME)/include/linux

LIBS = $(COMDRV_LIBS) $(LINKLIB) $(JDK_HOME)/jre/lib/amd64/server/libjvm.so

OBJS = $(COMDRV_OBJS)

MYOBJS = WCCOAJavaMain.o \
         WCCOAJavaDrv.o \
         WCCOAJavaResources.o \
         ../LibJava/Java.o \
         com_etm_api_jni_Driver.o \
         WCCOAJavaAlertService.o \
         WCCOAJavaConnection.o \
         WCCOAJavaDrvIntDp.o \
         WCCOAJavaHWMapper.o \
         WCCOAJavaHWService.o \
         WCCOAJavaTrans.o 

PVSS00WCCOAJava: $(MYOBJS)
	@rm -f addVerInfo.o
	@$(MAKE) addVerInfo.o
	mv Java.o ../LibJava
	$(LINK_CMD) -o WCCOAjavadrv addVerInfo.o $(MYOBJS) $(OBJS) $(LIBS)
	gcc -shared -gcc-11 -o libWCCOAjavadrv.so ../LibJava/Java.o *.o $(LIBS)

clean:
	@rm -f *.o WCCOAjavadrv WCCOAjavadrv.so ../LibJava/Java.o

addVerInfo.cxx: $(API_ROOT)/addVerInfo.cxx
	@cp -f $(API_ROOT)/addVerInfo.cxx addVerInfo.cxx

addVerInfo.o: $(OFILES) addVerInfo.cxx
    
