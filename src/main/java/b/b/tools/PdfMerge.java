package b.b.tools;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfMerge {

    // pdf合并工具类
    public static File mulFile2One(List<File> files, String targetPath) throws Exception {
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        for (File f : files) {
            if(f.exists() && f.isFile()){
                // 循环添加要合并的pdf
                mergePdf.addSource(f);
            }
        }
        // 设置合并生成pdf文件名称
        mergePdf.setDestinationFileName(targetPath);
        // 合并pdf
        mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        return new File(targetPath);
    }

    //测试
    public static void main(String[] args) throws IOException {
        List<File> files = new ArrayList();
        File file = new File("D:\\temp");
        File[] tempList = file.listFiles();
        //获取该文件夹下的文件（文件都是PDF）
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i]);
            }
        }
        try {
            File f = mulFile2One(files, "D:\\temp\\合成PDF.pdf");
            System.out.println(f.length());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
