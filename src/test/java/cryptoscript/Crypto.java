package cryptoscript;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ExcelReadNWrite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Crypto {

    final float budget = 20.00f;

    @Given("User running Crypto Script")
    public void user_running_Crypto_Script() throws IOException, InterruptedException {

        long timeInEpoch3 = System.currentTimeMillis();

//        for (int i = 1; i <= 3; i++) {
        for (int i = 1; i <= 100000; i++) {

            //Time in epoch
            long timeInEpoch = System.currentTimeMillis();
            String timeInEpochAsString = String.valueOf(timeInEpoch);

            //Time in readable format
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
//            System.out.println(dtf.format(now));
            String time = dtf.format(now);

            String min = time.substring(14, 16);
            int minInInt = Integer.parseInt(min);

            int rowNumber = 0;
            ExcelReadNWrite excelDoc = new ExcelReadNWrite("data/Data.xlsx", "Sheet1");
            float boughtVolume = 0;

//            boolean every15Mins = (minInInt == 00) || (minInInt == 15) || (minInInt == 30) || (minInInt == 45);

            RestAssured.baseURI = "https://api.binance.com";

            long timeInEpoch2 = System.currentTimeMillis();


//            if(every15Mins) {
            if ((timeInEpoch2 - 60000) >= timeInEpoch3) {


                Response transactionResponse1 =
                        RestAssured
                                .given()
                                .header("Content-Type", "text/plain")
                                .get("/api/v3/ticker/price?symbol=SHIBUSDT")
                                .then()
                                .extract()
                                .response();

                String asString = transactionResponse1.asString();
                JsonPath js = new JsonPath(asString);
                // System.out.println(js.prettify());
                String priceAsString = js.getString("price");
                float buyingPriceInUSDT = Float.parseFloat(priceAsString);

                System.out.println("General Round " + i);

                //Amount Bought
                boughtVolume = budget / buyingPriceInUSDT;
                String boughtVolumeAsString = String.valueOf(boughtVolume);


                //Bought-Budget amount in String
                String budgetAmount = String.valueOf(budget);

                excelDoc.createNewRow();
                rowNumber = excelDoc.getLastRowNumber();

                excelDoc.setColumnValue(timeInEpochAsString, 0, rowNumber);
                excelDoc.setColumnValue(time, 1, rowNumber);
                excelDoc.setColumnValue(priceAsString, 2, rowNumber);
                excelDoc.setColumnValue(boughtVolumeAsString, 3, rowNumber);
                excelDoc.setColumnValue(budgetAmount, 4, rowNumber);

                timeInEpoch3 = System.currentTimeMillis();

            }


            String excelColumn2 = null;
            String excelColumn3 = null;
            String excelColumn5 = null;

            float[] array2 = null;
            array2 = new float[rowNumber];
            float[] array3 = null;
            array3 = new float[rowNumber];
            String[] array5 = null;
            array5 = new String[rowNumber];

            for (int m = 1; m <= rowNumber; m++) {
//                System.out.println("Round of m -- " + m);
                array2[m - 1] = Float.parseFloat(excelDoc.getColumnStringValue(excelColumn2, 2, m));
                array3[m - 1] = Float.parseFloat(excelDoc.getColumnStringValue(excelColumn3, 3, m));
                array5[m - 1] = excelDoc.getColumnStringValue(excelColumn5, 5, m);
            }

            for (int j = 1; j <= 10; j++) {
//            for (int j = 1; j <= 2; j++) {


                ////Binance API Started
                Response transactionResponse2 =
                        RestAssured
                                .given()
                                .header("Content-Type", "text/plain")
                                .get("/api/v3/ticker/price?symbol=SHIBUSDT")
                                .then()
                                .extract()
                                .response();
                String asString2 = transactionResponse2.asString();
                JsonPath js2 = new JsonPath(asString2);
                String priceAsString2 = js2.getString("price");
                float sellingPriceInUSDT = Float.parseFloat(priceAsString2);



                for (int k = 1; k <= rowNumber; k++) {

                    float boughtPrice = array2[k - 1];

                    float boughtVolumeFromSpecRow = array3[k - 1];

//                    boolean execute = (((boughtPrice + 0.0000_0060f) <= sellingPriceInUSDT) || ((boughtPrice - 0.0000_0110f) >= sellingPriceInUSDT)) && ((array5[k - 1]) == null);
                    boolean execute = ((boughtPrice + 0.0000_0060f) <= sellingPriceInUSDT)  && ((array5[k - 1]) == null);
                    if (execute) {

                        float sellingPriceToExcell = sellingPriceInUSDT * boughtVolumeFromSpecRow;
                        String sellingPriceToExcellAsString = String.valueOf(sellingPriceToExcell);
                        String sellingPriceInUSDTAsString = String.valueOf(sellingPriceInUSDT);

                        excelDoc.setColumnValue(priceAsString2, 5, k);
                        excelDoc.setColumnValue(sellingPriceToExcellAsString, 6, k);


                    }

                }

            }


        }


    }
}
