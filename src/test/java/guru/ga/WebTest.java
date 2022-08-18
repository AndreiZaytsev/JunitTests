package guru.ga;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @BeforeAll
    static void configure() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
        open("https://www.sberbank.ru/ru/quotes/currencies");
        $(".kitt-cookie-warning__close").click();
    }


        @ValueSource(strings = {"EUR", "USD", "RUB"})
        @ParameterizedTest(name = "conversion from to {0}")
        void commonCurrencyConverterTest(String testData) {
         $("#ConverterFieldSell-RUB").click();
         $("#ConverterFieldGet-" + testData).click();
         $$(".rates-converter-field__text-currencies").first().shouldHave(text("1"));
         $$(".rates-converter-field__text-currencies").last().shouldHave(text("1"));
        }


        @CsvSource(value = {
                "EUR, 1 EUR =",
                "USD, 1 USD =",
                "RUB, 1 RUB =",
        })
        @ParameterizedTest(name = "conversion from rubles to {0} with output text {1}")
        void complexCurrencyConverterTest(String testData, String expectedResult) {
         $("#ConverterFieldSell-RUB").click();
         $("#ConverterFieldGet-" + testData).click();
         $$(".rates-converter-field__text-currencies").first().shouldHave(text("1"));
         $$(".rates-converter-field__text-currencies").last().shouldHave(text(expectedResult));
        }


   static Stream<Arguments> dataFullCurrencyConverterTest(){
       return Stream.of(
               Arguments.of("EUR", List.of("1 RUB = 0,0151 EUR", "1 EUR = 66,42 RUB")),
               Arguments.of("USD", List.of("1 RUB = 0,0152 USD", "1 USD = 65,71 RUB")),
               Arguments.of("RUB", List.of("1 RUB = 1 RUB", "1 RUB = 1 RUB"))

       );
   }
        @MethodSource("dataFullCurrencyConverterTest")
        @ParameterizedTest(name = "conversion from currency {0} to currency {1} is active")
        void fullCurrencyConverterTest(String currencyOutput, List<String> outputText) {
            $("#ConverterFieldSell-RUB").click();
            $("#ConverterFieldGet-" + currencyOutput).click();
            $$(".rates-converter-field__text-currencies").shouldHave(CollectionCondition.texts(outputText));
        }



}
