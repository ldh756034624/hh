package com.h9.admin;

import com.h9.admin.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gonyb on 2017/11/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test666 {
    @Resource
    private AccountService accountService;

    @Test
    public void contextLoads() {
        accountService.deviceIdInfo(new Date(0), new Date());
    }

    /*public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        for (String item : list) {
            if ("2".equals(item)) {
                list.remove(item);
            }
        }
    }*/
    @Test
    public void testRefund() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("apiclient_cert.p12");
        InputStream is2 = this.getClass().getClassLoader().getResourceAsStream("apiclient_cert.p12");


        File file = new File("/Users/ln/Documents/test.p12");

        if (!file.exists()) {
            file.createNewFile();
        }
        System.out.println("path :"+file.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(file);
        int len = 0;
        byte[] bytes = new byte[1024];

        while(( len = is.read(bytes)) != -1){
            fos.write(bytes, 0, len);
        }
        System.out.println("read finish!");
    }

}
