
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {




  private RestTemplate restTemplate;

  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  public PortfolioManagerImpl() {
  }


  //CHECKSTYLE:OFF
  static String getToken(){
    return "77be39d9ad5251e07ff9c7b654d6cc03b6dd1366";
  }
  

  
  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
     String url = buildUri(symbol, from, to);
     System.out.println(url);
     String response = restTemplate.getForObject(url, String.class);
     ObjectMapper mapper = new ObjectMapper();
     mapper.registerModule(new JavaTimeModule());
     TiingoCandle[] candles = mapper.readValue(response, TiingoCandle[].class);
     if(candles==null){
       return new ArrayList<>();
     }
     return Arrays.asList(candles);
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https:api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";
            return uriTemplate;
  }

  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    Candle firstCandle = candles .get(0);
    return firstCandle.getOpen();
  }

  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    int len = candles.size();
    Candle lastCandle = candles.get(len-1);
    return lastCandle.getClose();
  }

    

  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades, LocalDate endDate) throws JsonProcessingException{

    List<AnnualizedReturn> finalReturns = new ArrayList<>();
    
    for(PortfolioTrade pft : portfolioTrades){

      List<Candle> candles = getStockQuote(pft.getSymbol(), pft.getPurchaseDate(), endDate);
      Double buyPrice = getOpeningPriceOnStartDate(candles);
      Double sellPrice = getClosingPriceOnEndDate(candles);
      Double totalReturns = (sellPrice-buyPrice)/buyPrice;
     // System.out.println("BuyPrice:"+ buyPrice);
      //System.out.println("SellPrice:"+ sellPrice);
      //System.out.println("TotalReturns:"+ totalReturns);

      long numDays = ChronoUnit.DAYS.between(pft.getPurchaseDate(), endDate);
      Double numYears = numDays/365.24;
      Double annualizedReturns=Math.pow((1+totalReturns), (1/numYears))-1;
      //System.out.println("numYears:" + numYears);
      //System.out.println("annualizedReturns in:"+ annualizedReturns);

      AnnualizedReturn aReturn= new AnnualizedReturn(pft.getSymbol(), annualizedReturns, totalReturns);
      finalReturns.add(aReturn);
      // System.out.println("RS:"+ aReturn.getSymbol());
      // System.out.println("RA:"+ aReturn.getAnnualizedReturn());
      // System.out.println("RT:"+ aReturn.getTotalReturns());

    }
    
    finalReturns.sort(getComparator());
    for(int i=0;i<finalReturns.size();i++){
      // System.out.println("QS:"+ finalReturns.get(i).getSymbol());
      // System.out.println("QA:"+ finalReturns.get(i).getAnnualizedReturn());
      // System.out.println("QT:"+ finalReturns.get(i).getTotalReturns());
    }
    return finalReturns;

  }

  private Comparator<AnnualizedReturn> getComparator() {

    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }




}
