
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


class closePriceComparator implements Comparator<TotalReturnsDto>{
  @Override
  public int compare(TotalReturnsDto t1, TotalReturnsDto t2){
    if(t1.getClosingPrice() > t2.getClosingPrice()) 
    return 1;
    else if(t1.getClosingPrice() == t2.getClosingPrice())
    return 0;
    return -1;
  }
}

class annualizedReturnComparator implements Comparator<AnnualizedReturn>{
  @Override
  public int compare(AnnualizedReturn a1, AnnualizedReturn a2){
    if(a1.getAnnualizedReturn()<a2.getAnnualizedReturn())
    return 1;
    return -1;
  }
}


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Task:
  // - Read the json file provided in the argument[0], The file is available in
  // the classpath.
  // - Go through all of the "assessments/trades.json" given file,
  // - Prepare the list of all symbols a portfolio has.
  // - if "trades.json" has trades like
  // [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  // Then you should return ["MSFT", "AAPL", "GOOGL"]
  // Hints:
  // 1. Go through two functions provided - #resolveFileFromResources() and
  // #getObjectMapper
  // Check if they are of any help to you.
  // 2. Return the list of all symbols in the same order as provided in json.

  // Note:
  // 1. There can be few unused imports, you will need to fix them to make the
  // build pass.
  // 2. You can use "./gradlew build" to check if your code builds successfully.

  public static String getToken(){
    return "77be39d9ad5251e07ff9c7b654d6cc03b6dd1366";
  }

  private static String readFileAsString(String filename) throws IOException, URISyntaxException{
    return new String(Files.readAllBytes(resolveFileFromResources(filename).toPath()));
  }
  
  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    File file =   resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = getObjectMapper();
    PortfolioTrade[] trades = objectMapper.readValue(file, PortfolioTrade[].class);
    List<String> symbols = new ArrayList<>();
    for (PortfolioTrade t : trades) {
      symbols.add(t.getSymbol());
    }
     return symbols;
  }

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  // TODO: CRIO_TASK_MODULE_REST_API
  // Find out the closing price of each stock on the end_date and return the list
  // of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  // and deserialize the results in List<Candle>

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper objectMapper = new ObjectMapper();
    logger.info(objectMapper.writeValueAsString(object));
  }

 

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Follow the instructions provided in the task documentation and fill up the
  // correct values for
  // the variables provided. First value is provided for your reference.
  // A. Put a breakpoint on the first line inside mainReadFile() which says
  // return Collections.emptyList();
  // B. Then Debug the test #mainReadFile provided in
  // PortfoliomanagerApplicationTest.java

  // following the instructions to run the test.
  // Once you are able to run the test, perform following tasks and record the
  // output as a
  // String in the function below.
  // Use this link to see how to evaluate expressions -
  // https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  // 1. evaluate the value of "args[0]" and set the value
  // to the variable named valueOfArgument0 (This is implemented for your
  // reference.)
  // 2. In the same window, evaluate the value of expression below and set it
  // to resultOfResolveFilePathArgs0
  // expression ==> resolveFileFromResources(args[0])
  // 3. In the same window, evaluate the value of expression below and set it
  // to toStringOfObjectMapper.
  // You might see some garbage numbers in the output. Dont worry, its expected.
  // expression ==> getObjectMapper().toString()
  // 4. Now Go to the debug window and open stack trace. Put the name of the
  // function you see at
  // second place from top to variable functionNameFromTestFileInStackTrace
  // 5. In the same window, you will see the line number of the function in the
  // stack trace window.
  // assign the same to lineNumberFromTestFileInStackTrace
  // Once you are done with above, just run the corresponding test and
  // make sure its working as expected. use below command to do the same.
  // ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "assessment/trades.json";
    String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/mohammed-zahid019-ME_QMONEY_V2/qmoney/bin/test/assessment/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@77fbd92c";
    String functionNameFromTestFileInStackTrace = "ModuleOneTest.mainReadFile()";
    String lineNumberFromTestFileInStackTrace = "19";

    return Arrays.asList(new String[] { valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace });
  }

  // Note:
  // Remember to confirm that you are getting same results for annualized returns
  // as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {

    List<PortfolioTrade>listofallTrades = readTradesFromJson(args[0]);
    String token = "77be39d9ad5251e07ff9c7b654d6cc03b6dd1366";
    LocalDate endDate = LocalDate.parse(args[1]);

    List<TotalReturnsDto> closeCandle = new ArrayList<>();


    for(PortfolioTrade trade : listofallTrades){

      String tradeUrl = prepareUrl(trade, endDate, token);

      String symbol = trade.getSymbol();
      RestTemplate restTemplate = new RestTemplate();

     
      TiingoCandle[] allCandles = restTemplate.getForObject(tradeUrl,TiingoCandle[].class);
      
        closeCandle.add(new TotalReturnsDto(symbol, allCandles[allCandles.length-1].getClose()));
      }
    
    //Collections.sort(data,TotalReturnsDto);
      List<String> sortedTradesSymbol = new ArrayList<>();
      Collections.sort(closeCandle,new closePriceComparator());

    for(TotalReturnsDto tempDto :closeCandle){
      sortedTradesSymbol.add(tempDto.getSymbol());
    }
     return sortedTradesSymbol;
  }

  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException{
    File tradesFile = resolveFileFromResources(filename);

    ObjectMapper mapper = getObjectMapper();

    PortfolioTrade[] allTrades = mapper.readValue(tradesFile, PortfolioTrade[].class);

    List<PortfolioTrade> listofTrades = Arrays.asList(allTrades);
    return listofTrades;
  }

  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
   
  return "https://api.tiingo.com/tiingo/daily/"  + trade.getSymbol() + "/prices?startDate=" + trade.getPurchaseDate()
   + "&endDate=" + endDate + "&token=" + token;
}



  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
     Candle firstCandle = candles .get(0);
     return firstCandle.getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    int len = candles.size();
    Candle lastCandle = candles.get(len-1);
    return lastCandle.getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {

    String symbolUrl = "https://api.tiingo.com/tiingo/daily/" + trade.getSymbol() +"/prices?startDate=" + trade.getPurchaseDate()
     + "&endDate=" + endDate + "&token=" + token;

    RestTemplate restTemplate = new RestTemplate();

    Candle[] allCandle = restTemplate.getForObject(symbolUrl, TiingoCandle[].class);
    List<Candle> listofCandle = Arrays.asList(allCandle);
    return listofCandle;

  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {

        List<PortfolioTrade> listofallTrades = readTradesFromJson(args[0]);
        String token = "77be39d9ad5251e07ff9c7b654d6cc03b6dd1366";
        LocalDate endDate = LocalDate.parse(args[1]);
        List<AnnualizedReturn> allReturns = new ArrayList<>();

        for(PortfolioTrade trade : listofallTrades){
          
          String tradeUrl = prepareUrl(trade, endDate, token);

          RestTemplate restTemplate = new RestTemplate();
          Candle[] allCandles = restTemplate.getForObject(tradeUrl, TiingoCandle[].class);

          List<Candle> listofCandles = Arrays.asList(allCandles);

          Double sellingPrice = getClosingPriceOnEndDate(listofCandles);
          Double purchasePrice = getOpeningPriceOnStartDate(listofCandles);

          allReturns.add(calculateAnnualizedReturns(endDate,trade,purchasePrice,sellingPrice));
        }
        Collections.sort(allReturns, new annualizedReturnComparator());
        return allReturns;
      }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {

        Double totalReturn = (sellPrice - buyPrice)/buyPrice;
        LocalDate startDate = trade.getPurchaseDate();
        String symbol = trade.getSymbol();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        Double yearBetween = ((double)daysBetween/365.24);
        Double annualizedReturn = Math.pow(1+totalReturn,(double)(1/yearBetween))-1;

        return new AnnualizedReturn(symbol,annualizedReturn, totalReturn);
  }
  

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       LocalDate endDate = LocalDate.parse(args[1]);
       String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       PortfolioTrade[] portfolioTrades = objectMapper.readValue(contents, PortfolioTrade[].class);
       PortfolioManager pManager = PortfolioManagerFactory.getPortfolioManager(new RestTemplate());
       return PortfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    printJsonObject(mainCalculateSingleReturn(args));
    printJsonObject(mainReadFile(args));
    printJsonObject(mainReadQuotes(args));
    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }

}

