package com.dqm;

import com.dqm.msg.Transaction;
import com.dqm.utils.ByteUtil;
import com.dqm.utils.CodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by dqm on 2018/8/23.
 */
@CommandLine.Command(name = "gettx", version = "")
public class TranTest implements Runnable {

    @CommandLine.Option(names = { "-v", "--verbose" }, description = "详细说明")
    private boolean verbose;

    @CommandLine.Parameters(index = "0..", description = "交易列表")
    private List<String> txs;

    public void run() {
        if (txs.size() > 0) {
            System.out.println("查找 " + txs.size() + " 个 transactions ...");

            System.out.println();
            for (String tx : txs) {
                System.out.println("交易【" + tx + "】结果如下：");
                System.out.println(findTx(tx));
                System.out.println();
            }
        }
    }

    public static final void main(String[] args) throws Exception {
        File file = new File("/Users/dqm/Downloads/2dc4031a55c38ba93d74fb6b7d881f930b78f389a3bc548acc2fd18c532b3907.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while(bufferedReader.ready()) {
            String tmp = bufferedReader.readLine();

            byte[] test = ByteUtil.hexStringToByte(tmp);
            ByteBuffer byteBuffer = ByteBuffer.wrap(test).order(ByteOrder.BIG_ENDIAN);
            Transaction transaction = CodeUtil.decode(byteBuffer, Transaction.class);

            ObjectMapper om = new ObjectMapper();
            System.out.print(om.writeValueAsString(transaction));
        }
        fileReader.close();
        bufferedReader.close();
    }

    private static String findTx(String tx) {
        try {
            HttpGet httpGet = new HttpGet("http://learnmeabitcoin.com/browser/transaction/download.php?txid=" + tx);
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String tmp = EntityUtils.toString(httpEntity);

            byte[] test = ByteUtil.hexStringToByte(tmp);

            ByteBuffer byteBuffer = ByteBuffer.wrap(test).order(ByteOrder.BIG_ENDIAN);
            Transaction transaction = CodeUtil.decode(byteBuffer, Transaction.class);

            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return "异常了...";
        }
    }
}
