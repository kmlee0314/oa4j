import at.rocworks.oa4j.base._
import at.rocworks.oa4j.`var`._
import at.rocworks.oa4j.utils.Debug

/**
  * Created by vogler on 2/21/2017.
  */
object Test {
  def main(args: Array[String]) {
    // add path to WCCOAjava.dll to your path environment!
    // logs are printed to WCCOAjava<num>.0.log and WCCOAjava10.err
    val m = (new JManager).init(args)
    m.start()
    Debug.out.info("runSet...")
    runSet()
    Debug.out.info("runGet...")
    runGet()
    Debug.out.info("runConnect...")
    runConnect()
    Debug.out.info("stop...")
    m.stop()
    Debug.out.info("done.")
  }

  def runGet(): Unit = {
    val v1 = JClient.dpGet.add("ExampleDP_Trend1.").await()
    Debug.out.info("v1: "+v1.toString)

    val v2 = JClient.dpGet("ExampleDP_Trend1.");
    Debug.out.info("v2: "+v2.toString)

    val v3var = new VariablePtr()
    val v3ret = JClient.dpGet("ExampleDP_Trend1.", v3var);
    Debug.out.info(s"v3: ret=$v3ret value="+v3var.get.toString)
  }

  def runSet(): Unit = {
    JClient.dpSet()
      .add("ExampleDP_Trend1.", new FloatVar(1))
      .add("ExampleDP_SumAlert.", "hello world!")
      .send()
    Thread.sleep(1000);

    JClient.dpSet("ExampleDP_Trend1.", 2.0);
    Thread.sleep(1000);

    JClient.dpSet("ExampleDP_Trend1.", new FloatVar(3))
    Thread.sleep(1000);

    JClient.dpSetWait("ExampleDP_SumAlert.", "hello scala world!")
    Thread.sleep(1000);
  }

  def runConnect() {
    Debug.out.info("dpConnect...")

    val conn = JClient.dpConnect.add("ExampleDP_Trend1.")

    conn.answer((answer) => {
        Debug.out.info("--- ANSWER BEG ---")
        Debug.out.info(answer.toString())
        Debug.out.info("--- ANSWER END ---")
      }
    )

    conn.hotlink((hotlink) => {
        Debug.out.info("--- HOTLINK BEG ---")
        Debug.out.info(hotlink.toString())
        Debug.out.info("--- HOTLINK END ---")
      }
    )

    conn.connect()
    Debug.out.info("sleep...")
    Thread.sleep(1000 * 60)
    Debug.out.info("done")
    conn.disconnect()
  }
}
