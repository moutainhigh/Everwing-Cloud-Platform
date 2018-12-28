package gettingstarted;/**
 * Created by wust on 2018/2/28.
 */

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *
 * Function:多个sheet页
 * Reason:
 * Date:2018/2/28
 * @author wusongti@lii.com.cn
 */
public class SimpleReport_MultipleSheets {
    public SimpleReport_MultipleSheets() {
        build();
    }

    private void build() {
        JasperXlsExporterBuilder xlsExporter = export.xlsExporter("D:/report1.xls").addSheetName("wusongti").addSheetName("zhangsan").setOnePagePerSheet(true);

        StyleBuilder style = stl.style(stl.pen1Point())
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
        VerticalListBuilder a = cmp.verticalList();
        VerticalListBuilder b = cmp.verticalList();
        for (int i = 0; i < 10; i++) {
            TextFieldBuilder<String> textField = cmp.text("Title component " + (i + 1));
            a.add(textField);

            TextFieldBuilder<String> textField1 = cmp.text("Title component1 " + (i + 1));
            b.add(textField1);
        }

        try {
            report()
                    .setTemplate(Templates.reportTemplate)
                    .title(cmp.text("A"),a,cmp.pageBreak(),cmp.text("B"),b)
                    //.summary(c)
                    //.pageFooter(Templates.footerComponent)
                    .toXls(xlsExporter);
        } catch (DRException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new SimpleReport_MultipleSheets();
    }
}
