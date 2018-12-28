/**
 * Created by wust on 2018/7/27.
 */


import com.everwing.coreservice.common.utils.RC4;

/**
 *
 * Function:
 * Reason:
 * Date:2018/7/27
 * @author wusongti@lii.com.cn
 */
public class RC4Test {
   public static void main(String[] args){




      String a = RC4.encry_RC4_string("XH_WY_LOGIN_QR_CODE.8544fc78-d723-42c8-a22c-1263e25d62d3.c6962a2b-003c-4e05-a5c2-2581ac110f97.test", "jZ5$x!6yeAo1Qe^r_");

      String b = RC4.decry_RC4("0e005a3c5240fe92c7332e01aa94df328556f9ed565bcfbcda321464a9fc8acc23ac8d57dbdfbb5ce796ac1606f70f97334dc7f3841eb5e6deec276f6c3f14a2c53429cd6b2effcd0556bb5c6573de651f5299bb91e8674789f242215a770a44fa5c9336ad1972a256","jZ5$x!6yeAo1Qe^r_");

      System.out.println("a=" + a);
      System.out.println("b=" + b);
   }
}
