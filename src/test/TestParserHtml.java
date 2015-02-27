package test;

import android.test.AndroidTestCase;
import android.text.Html;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import ua.maker.sinopticua.models.ItemDetail;
import ua.maker.sinopticua.models.WeatherStruct;
import ua.maker.sinopticua.utils.DataParser;

/**
 * Created by Daniil on 25.02.2015.
 */
public class TestParserHtml extends AndroidTestCase {

    public static final String TAG = "TestParserHtml";

    public String site = "<!DOCTYPE html>\n" +
            "<!--[if IE 8]>    <html class=\"lt-ie9\" xmlns:fb=\"//ogp.me/ns/fb#\"> <![endif]-->\n" +
            "<!--[if gt IE 8]><!--><html xmlns:fb=\"//ogp.me/ns/fb#\"><!--<![endif]-->\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <title>SINOPTIK: Погода в Украине, подробный прогноз погоды на неделю. Погода сегодня, завтра в Украине и Мире.</title>\n" +
            "    <meta name=\"title\" content=\"SINOPTIK: Погода в Украине, подробный прогноз погоды на неделю. Погода сегодня, завтра в Украине и Мире.\" />\n" +
            "<meta name=\"description\" content=\"Погода в Украине (во всех 29 815 населенных пунктах) на неделю, прогноз погоды по 104 000+ городов Мира на 10 дней. Подробный прогноз погоды для Вашего города от SINOPTIK.\" />\n" +
            "<meta name=\"keywords\" content=\"погода, прогноз погоды, погода в Украине, прогноз погоды в Украине, погода на неделю, погода сегодня, погода завтра, погода на неделю, погода на 10 дней, погода на выходные\" />\n" +
            "\n" +
            "    <meta name=\"google-site-verification\" content=\"Aa5Fv5m0AJ_siRUzD4fBe89qeXDIgHw_z-IlneNWUb8\" />\n" +
            "            <meta property=\"og:title\" content=\"Погода в Краматорске\"/>\n" +
            "        <meta property=\"og:type\" content=\"company\"/>\n" +
            "        <meta property=\"og:url\" content=\"//sinoptik.ua/погода-краматорск\"/>\n" +
            "        <meta property=\"og:image\" content=\"//sinst.fwdcdn.com/img/newImg/sinLogo.png\"/>\n" +
            "        <meta property=\"og:site_name\" content=\"SINOPTIK.UA\"/>\n" +
            "        <meta property=\"og:description\" content=\"Прогноз погоды в Краматорске\"/>\n" +
            "        <meta property=\"fb:app_id\" content=\"243438275713069\" />\n" +
            "        <link type=\"text/css\" rel=\"stylesheet\" href=\"//sinst.fwdcdn.com/css/86/main.css\"/>\n" +
            "    <link rel=\"apple-touch-icon\" href=\"//sinst.fwdcdn.com/img/newImg/touch-icon-iphone.png\">\n" +
            "    <link rel=\"apple-touch-icon\" sizes=\"76x76\" href=\"//sinst.fwdcdn.com/img/newImg/touch-icon-ipad.png\">\n" +
            "    <link rel=\"apple-touch-icon\" sizes=\"120x120\" href=\"//sinst.fwdcdn.com/img/newImg/touch-icon-iphone-retina.png\">\n" +
            "    <link rel=\"apple-touch-icon\" sizes=\"152x152\" href=\"//sinst.fwdcdn.com/img/newImg/touch-icon-ipad-retina.png\">\n" +
            "</head>\n" +
            "<body class=\"ru\" >\n" +
            "<div id=\"wrapper\">\n" +
            "    <div class=\"b-top_place\"><div id=\"adriver_728x90_sinoptik\"></div></div>    <div id=\"header\" class=\"clearfix \">\n" +
            "        <div class=\"bLogo\">\n" +
            "            <img src=\"//sinst.fwdcdn.com/img/newImg/sinoptic-logo.png\"/>\n" +
            "            <a class=\"sLogo\" href=\"//sinoptik.ua/\">Прогноз погоды</a>\n" +
            "        </div>\n" +
            "        <form id=\"form-search\" action=\"//sinoptik.ua/redirector\" method=\"get\">\n" +
            "            <p class=\"clearfix\">\n" +
            "                <input autocomplete=\"off\" id=\"search_city\" name=\"search_city\" type=\"text\" placeholder=\"Название населенного пункта, страны или региона\" value=\"\" />\n" +
            "                <input class=\"search_city-submit\" type=\"submit\" value=\"Погода\"/>\n" +
            "            </p>\n" +
            "            <p id=\"form-search-examples\"></p>\n" +
            "        </form>\n" +
            "        <a class=\"itypeSwitcher\" style=\"display:none;\" href=\"javascript:;\" onclick=\"SIN.utility.cookie('_itype','smart',{expires:1});location.reload();\">Мобильная версия сайта</a>\n" +
            "        <div id=\"sLang\">\n" +
            "            Sinoptik.ua\n" +
            "                            <a href=\"//ua.sinoptik.ua/\">українською</a>\n" +
            "                    </div>\n" +
            "                    <!--noindex-->\n" +
            "                            <div class=\"informerTop js-informerTop\"></div>\n" +
            "                        <!--/noindex-->\n" +
            "                <div class=\"cityName cityNameShort\">\n" +
            "                        <h1 class=\"isMain\">\n" +
            "                <strong>Погода</strong> в Краматорске            </h1>\n" +
            "                <div class=\"currentRegion\">Донецкая область</div>\n" +
            "</div>\n" +
            "<div id=\"topMenu\" class=\"dCatalogOpen\">\n" +
            "                        <span class=\"tMenu\">\n" +
            "                7 дней            </span>\n" +
            "                <a class=\"menu-item\" href=\"//sinoptik.ua/10-дней\">\n" +
            "            10 дней        </a>\n" +
            "                <a class=\"otherCity\" href=\"//sinoptik.ua/украина/донецкая-область\">\n" +
            "            выбрать другой город        </a>\n" +
            "    </div>\n" +
            "    </div>\n" +
            "    <!--block update GMT+2: 2015-02-25 13:11:46-->        <div id=\"content\" class=\"clearfix\">\n" +
            "        <div id=\"leftCol\">\n" +
            "            <div id=\"mainContentBlock\"><div id=\"blockDays\" data-city-id=\"303012261\" class=\"bd1\">\n" +
            "    <div class=\"tabs\">\n" +
            "        <div class=\"first\">&nbsp;</div>\n" +
            "                    <div class=\"main loaded\" id=\"bd1\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-02-25\" href=\"//sinoptik.ua/погода-краматорск/2015-02-25\">Среда</a></p>\n" +
            "                <p class=\"date \">25</p>\n" +
            "                                    <p class=\"month\">февраля</p>\n" +
            "                                <div class=\"weatherIco d100\" title=\"Небольшая облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d100.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>-1&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+7&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid1\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd2\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-02-26\" href=\"//sinoptik.ua/погода-краматорск/2015-02-26\">Четверг</a></p>\n" +
            "                <p class=\"date \">26</p>\n" +
            "                                    <p class=\"month\">февраля</p>\n" +
            "                                <div class=\"weatherIco d310\" title=\"Облачно с прояснениями, мелкий дождь\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d310.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>-1&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+8&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid2\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd3\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-02-27\" href=\"//sinoptik.ua/погода-краматорск/2015-02-27\">Пятница</a></p>\n" +
            "                <p class=\"date \">27</p>\n" +
            "                                    <p class=\"month\">февраля</p>\n" +
            "                                <div class=\"weatherIco d100\" title=\"Небольшая облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d100.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>+3&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+14&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid3\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd4\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-02-28\" href=\"//sinoptik.ua/погода-краматорск/2015-02-28\">Суббота</a></p>\n" +
            "                <p class=\"date dateFree\">28</p>\n" +
            "                                    <p class=\"month\">февраля</p>\n" +
            "                                <div class=\"weatherIco d400\" title=\"Сплошная облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d400.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>0&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+4&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid4\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd5\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-03-01\" href=\"//sinoptik.ua/погода-краматорск/2015-03-01\">Воскресенье</a></p>\n" +
            "                <p class=\"date dateFree\">01</p>\n" +
            "                                    <p class=\"month\">марта</p>\n" +
            "                                <div class=\"weatherIco d300\" title=\"Облачно с прояснениями\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d300.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>0&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+7&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid5\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd6\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-03-02\" href=\"//sinoptik.ua/погода-краматорск/2015-03-02\">Понедельник</a></p>\n" +
            "                <p class=\"date \">02</p>\n" +
            "                                    <p class=\"month\">марта</p>\n" +
            "                                <div class=\"weatherIco d100\" title=\"Небольшая облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d100.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>-4&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+5&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"mid6\">&nbsp;</div>\n" +
            "                                <div class=\"main \" id=\"bd7\">\n" +
            "                <p><a class=\"day-link\" data-link=\"#погода-краматорск/2015-03-03\" href=\"//sinoptik.ua/погода-краматорск/2015-03-03\">Вторник</a></p>\n" +
            "                <p class=\"date \">03</p>\n" +
            "                                    <p class=\"month\">марта</p>\n" +
            "                                <div class=\"weatherIco d400\" title=\"Сплошная облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/m/d400.gif\" alt=\"\" /></div>\n" +
            "\t\t                <div class =\"temperature\">\n" +
            "                    <div class=\"min\">мин. <span>0&deg;</span></div>\n" +
            "                    <div class=\"max\">макс. <span>+7&deg;</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "                            <div class=\"last\">&nbsp;</div>\n" +
            "    </div>\n" +
            "  <div class =\"tabsTop\"><div class =\"lc\"><div class =\"rc\"></div></div></div>\n" +
            "\n" +
            "  <div class=\"tabsContent\"><div class=\"tabsContentInner\">\n" +
            "            <div class=\"Tab\" id=\"bd1c\"><div class=\"wMain clearfix\">\n" +
            "    <div class=\"lSide\">\n" +
            "                    <div class=\"imgBlock\">\n" +
            "                <p class=\"today-time\">Погода сегодня в 14:00</p>\n" +
            "                <div class=\"img\">\n" +
            "                    <img width=\"188\" height=\"150\" src=\"//sinst.fwdcdn.com/img/weatherImg/b/d300.jpg\" alt=\"Облачно с прояснениями\" />\n" +
            "                </div>\n" +
            "                <div class=\"therm\"><span style=\"height:47px;\"></span></div>\n" +
            "                <p class=\"today-temp\">+7&deg;C</p>\n" +
            "                            </div>\n" +
            "                            <div class=\"infoDaylight\">\n" +
            "                                    Восход <span>06:17</span>\n" +
            "                    Закат <span>17:08</span>\n" +
            "                            </div>\n" +
            "                <div class=\"titles\">\n" +
            "            <p>Температура, C</p>\n" +
            "            <p><span class=\"Tooltip\" data-tooltip=\"как будет ощущаться<br /> температура воздуха<br /> человеком, одетым по сезону\">чувствуется как</span></p>            <p>Давление, мм</p>\n" +
            "            <p>Влажность, %</p>\n" +
            "            <p>Ветер, м/сек</p>\n" +
            "            <p>Вероятность<br /> осадков, %</p>        </div>\n" +
            "    </div>\n" +
            "    <div class=\"rSide\">\n" +
            "                <table class=\"weatherDetails\">\n" +
            "            <thead>\n" +
            "            <tr>\n" +
            "                                    <td class=\"\" colspan=\"2\">ночь</td>\n" +
            "                                    <td class=\"\" colspan=\"2\">утро</td>\n" +
            "                                    <td class=\"\" colspan=\"2\">день</td>\n" +
            "                                    <td class=\"noB\" colspan=\"2\">вечер</td>\n" +
            "                            </tr>\n" +
            "            </thead>\n" +
            "            <tbody>\n" +
            "            <tr class=\"gray time\">\n" +
            "                                        <td class=\"p1  \" >2:00</td>\n" +
            "                                                <td class=\"p2 bR \" >5:00</td>\n" +
            "                                                <td class=\"p3  \" >8:00</td>\n" +
            "                                                <td class=\"p4 bR \" >11:00</td>\n" +
            "                                                <td class=\"p5  cur\" >14:00</td>\n" +
            "                                                <td class=\"p6 bR \" >17:00</td>\n" +
            "                                                <td class=\"p7  \" >20:00</td>\n" +
            "                                                <td class=\"p8  \" >23:00</td>\n" +
            "                                    </tr>\n" +
            "            <tr class=\"img weatherIcoS\">\n" +
            "                                        <td class=\"p1  \" >\n" +
            "                            <div class=\"weatherIco n000\" title=\"Ясно\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/n000.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p2 bR \" >\n" +
            "                            <div class=\"weatherIco n000\" title=\"Ясно\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/n000.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p3  \" >\n" +
            "                            <div class=\"weatherIco d100\" title=\"Небольшая облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/d100.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p4 bR \" >\n" +
            "                            <div class=\"weatherIco d100\" title=\"Небольшая облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/d100.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p5  cur\" >\n" +
            "                            <div class=\"weatherIco d300\" title=\"Облачно с прояснениями\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/d300.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p6 bR \" >\n" +
            "                            <div class=\"weatherIco d200\" title=\"Переменная облачность\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/d200.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p7  \" >\n" +
            "                            <div class=\"weatherIco n300\" title=\"Облачно с прояснениями\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/n300.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                                <td class=\"p8  \" >\n" +
            "                            <div class=\"weatherIco n300\" title=\"Облачно с прояснениями\"><img class=\"weatherImg\" src=\"//sinst.fwdcdn.com/img/weatherImg/s/n300.gif\" alt=\"\" /></div>\n" +
            "                        </td>\n" +
            "                                    </tr>\n" +
            "            <tr class=\"temperature\">\n" +
            "                                        <td class=\"p1  \" >0&deg;</td>\n" +
            "                                                <td class=\"p2 bR \" >-1&deg;</td>\n" +
            "                                                <td class=\"p3  \" >+1&deg;</td>\n" +
            "                                                <td class=\"p4 bR \" >+6&deg;</td>\n" +
            "                                                <td class=\"p5  cur\" >+7&deg;</td>\n" +
            "                                                <td class=\"p6 bR \" >+3&deg;</td>\n" +
            "                                                <td class=\"p7  \" >+1&deg;</td>\n" +
            "                                                <td class=\"p8  \" >+1&deg;</td>\n" +
            "                                    </tr>\n" +
            "                            <tr class=\"temperatureSens\">\n" +
            "                                                <td class=\"p1  \" >-3&deg;</td>\n" +
            "                                                        <td class=\"p2 bR \" >-1&deg;</td>\n" +
            "                                                        <td class=\"p3  \" >-2&deg;</td>\n" +
            "                                                        <td class=\"p4 bR \" >+2&deg;</td>\n" +
            "                                                        <td class=\"p5  cur\" >+4&deg;</td>\n" +
            "                                                        <td class=\"p6 bR \" >0&deg;</td>\n" +
            "                                                        <td class=\"p7  \" >-2&deg;</td>\n" +
            "                                                        <td class=\"p8  \" >-2&deg;</td>\n" +
            "                                            </tr>\n" +
            "                        <tr class=\"gray\">\n" +
            "                                        <td class=\"p1  \" >758</td>\n" +
            "                                                <td class=\"p2 bR \" >758</td>\n" +
            "                                                <td class=\"p3  \" >758</td>\n" +
            "                                                <td class=\"p4 bR \" >758</td>\n" +
            "                                                <td class=\"p5  cur\" >758</td>\n" +
            "                                                <td class=\"p6 bR \" >758</td>\n" +
            "                                                <td class=\"p7  \" >759</td>\n" +
            "                                                <td class=\"p8  \" >760</td>\n" +
            "                                    </tr>\n" +
            "            <tr>\n" +
            "                                        <td class=\"p1  \" >69</td>\n" +
            "                                                <td class=\"p2 bR \" >70</td>\n" +
            "                                                <td class=\"p3  \" >62</td>\n" +
            "                                                <td class=\"p4 bR \" >47</td>\n" +
            "                                                <td class=\"p5  cur\" >49</td>\n" +
            "                                                <td class=\"p6 bR \" >72</td>\n" +
            "                                                <td class=\"p7  \" >73</td>\n" +
            "                                                <td class=\"p8  \" >62</td>\n" +
            "                                    </tr>\n" +
            "            <tr class=\"gray\">\n" +
            "                                        <td class=\"p1  \" ><div data-tooltip=\"Юго-восточный, 3.0 м/с \" class=\"Tooltip wind wind-SE\">3.0</div></td>\n" +
            "                                                <td class=\"p2 bR \" ><div data-tooltip=\"Восточный, 1.0 м/с \" class=\"Tooltip wind wind-E\">1.0</div></td>\n" +
            "                                                <td class=\"p3  \" ><div data-tooltip=\"Юго-восточный, 3.0 м/с \" class=\"Tooltip wind wind-SE\">3.0</div></td>\n" +
            "                                                <td class=\"p4 bR \" ><div data-tooltip=\"Восточный, 6.0 м/с \" class=\"Tooltip wind wind-E\">6.0</div></td>\n" +
            "                                                <td class=\"p5  cur\" ><div data-tooltip=\"Юго-восточный, 5.6 м/с \" class=\"Tooltip wind wind-SE\">5.6</div></td>\n" +
            "                                                <td class=\"p6 bR \" ><div data-tooltip=\"Юго-восточный, 2.4 м/с \" class=\"Tooltip wind wind-SE\">2.4</div></td>\n" +
            "                                                <td class=\"p7  \" ><div data-tooltip=\"Юго-восточный, 2.9 м/с \" class=\"Tooltip wind wind-SE\">2.9</div></td>\n" +
            "                                                <td class=\"p8  \" ><div data-tooltip=\"Юго-восточный, 2.9 м/с \" class=\"Tooltip wind wind-SE\">2.9</div></td>\n" +
            "                                    </tr>\n" +
            "                            <tr>\n" +
            "                                                <td class=\"p1  \" >-</td>\n" +
            "                                                        <td class=\"p2 bR \" >-</td>\n" +
            "                                                        <td class=\"p3  \" >-</td>\n" +
            "                                                        <td class=\"p4 bR \" >-</td>\n" +
            "                                                        <td class=\"p5  cur\" >2</td>\n" +
            "                                                        <td class=\"p6 bR \" >2</td>\n" +
            "                                                        <td class=\"p7  \" >2</td>\n" +
            "                                                        <td class=\"p8  \" >2</td>\n" +
            "                                            </tr>\n" +
            "                        </tbody>\n" +
            "        </table>\n" +
            "    </div>\n" +
            "</div>\n" +
            "    <div class=\"oWarnings clearfix\">\n" +
            "        <div class=\"lSide\"></div>\n" +
            "        <div class=\"rSide\">\n" +
            "                            <div class=\"description ico-stormWarning-3 ico-stormWarning-wide ico-stormWarning-short\">Днем местами порывы ветра 15-20 м/с</div>\n" +
            "                    </div>\n" +
            "    </div>\n" +
            "<div class=\"wDescription clearfix\">\n" +
            "    <div class=\"lSide\">\n" +
            "                                </div>\n" +
            "    <div class=\"rSide\">\n" +
            "        <div class=\"description\">\n" +
            "                            <!--noindex-->\n" +
            "                    Первая половина дня в Краматорске была ясной, но после обеда появились облака, которые до конца дня так никуда и не исчезнут. Без осадков.                <!--/noindex-->\n" +
            "                    </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div class=\"oDescription clearfix\">\n" +
            "    <div class=\"lSide\">\n" +
            "                                    <p class=\"infoHistory\">за последние 130 лет<br/>25 февраля:</p>\n" +
            "                <p class=\"infoHistoryval\">\n" +
            "                    <i>макс</i>: <span>14.3&deg;</span> (1990)<br/>\n" +
            "                    <i>мин</i>: <span>-23.1&deg;</span> (1945)\n" +
            "                </p>\n" +
            "                        </div>\n" +
            "    <div class=\"rSide\">\n" +
            "        <!--noindex-->\n" +
            "        <div class=\"description\"><h2>Народный прогноз погоды</h2>: 25 февраля почитается память святого Алексия, чудотворца. В народе 25 февраля назывался днем Алексея Рыбного, потому что если в этот день наступала оттепель — летом будет хорошо ловиться рыба. С этого дня в течение трех дней выставляли на утренний мороз зерно, предназначенное для посева, говоря, что тронутые морозом семена дают лучший урожай. Если на Алексея небо звездно, можно ждать хлеборода. А солнечный день обещал хороший урожай яблок.</div>\n" +
            "        <!--/noindex-->\n" +
            "        <div class=\"lastYear\">\n" +
            "                                                <a href=\"//sinoptik.ua/погода-краматорск/2014-02-25\">Погода в Краматорске год назад</a>\n" +
            "                                    </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "</div>\n" +
            "    <div class=\"Tab\" id=\"bd2c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd3c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd4c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd5c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd6c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd7c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd8c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd9c\"></div>\n" +
            "    <div class=\"Tab\" id=\"bd10c\"></div>\n" +
            "    <div class=\"social\">\n" +
            "                </div>\n" +
            "  </div></div>\n" +
            "  <div class =\"tabsBottom\"><div class =\"lc\"><div class =\"rc\"></div></div></div>\n" +
            "</div></div>            <!--noindex-->\n" +
            "                                                <div id=\"admixer_async_2027083\"></div>\n" +
            "                    <div style=\"position:absolute;visibility:hidden;height:0;\"><iframe id=\"ar_container_1\" width=1 height=1 marginwidth=0 marginheight=0 scrolling=no frameborder=0></iframe></div><div id=\"ad_ph_1\" class=\"banerBlock1\"></div>\n" +
            "                                <div id=\"adsLeftZone\"></div>\n" +
            "                        <!--/noindex-->\n" +
            "            <div id=\"tenOtherCities\">    <div class=\"b-cities-weather\">\n" +
            "        <h2 class=\"b-cities-weather-title\">\n" +
            "\t\t\t                                    <a href=\"//sinoptik.ua/украина/донецкая-область\">\n" +
            "                        Погода в Донецкой области:\n" +
            "                    </a>\n" +
            "                                    </h2>\n" +
            "                <div class=\"weatherIcoS clearfix\">\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +5&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-донецк\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Донецке                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +8&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красная-гора\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красной Горе                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +5&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красная-заря-303012309\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красной Заре                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +5&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красная-поляна\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красной Поляне                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +5&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красноармейск\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красноармейске                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +6&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красноармейское-303012329\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красноармейском                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +6&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красновка\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красновке                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +5&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красногоровка\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красногоровке                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +7&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красное\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красном                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +6&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красноподолье\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красноподолье                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +9&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-краснополовка\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Краснополовке                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +9&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-краснополье-303012452\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Краснополье                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d300\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Облачно с прояснениями\" />\n" +
            "                        +7&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красноторка\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красноторке                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +6&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красноярское-303012500\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красноярском                                            </a>\n" +
            "                </div>\n" +
            "                            <div class=\"b-cities-weather-item\">\n" +
            "                                            <img class=\"weatherIco d200\" src=\"//sinst.fwdcdn.com/img/t.gif\" alt=\"\" title=\"Переменная облачность\" />\n" +
            "                        +6&deg;\n" +
            "                                        <a class=\"cityLink\" href=\"//sinoptik.ua/погода-красный-кут-303012512\">\n" +
            "                                                    <!--noindex-->Погода<!--/noindex--> в Красном Куте                                            </a>\n" +
            "                </div>\n" +
            "                    </div>\n" +
            "                    <a class=\"b-cities-weather-link\" rel=\"nofollow\" href=\"//sinoptik.ua/украина\">\n" +
            "                Погода на неделю в других городах Украины            </a>\n" +
            "            </div>\n" +
            "</div>\n" +
            "            <!--noindex-->\n" +
            "                            <div id=\"adsLeftZone3\"></div>\n" +
            "                        <!--/noindex-->\n" +
            "            <!--noindex-->\n" +
            "<div class=\"informAd isMain\">\n" +
            "    <p >Хотите, чтобы на вашем сайте показывалась <strong>погода</strong> <br/><span>в Краматорске</span>?</p>\n" +
            "    <a href=\"//sinoptik.ua/информеры?id=303012261\">Настройте информер и получите код</a>\n" +
            "</div>\n" +
            "<!--/noindex-->\n" +
            "        </div>\n" +
            "        <div id=\"rightCol\">\n" +
            "            <!--noindex-->\n" +
            "                                                                        <div id=\"admixer_async_657326783\"></div>\n" +
            "                        <div id=\"adriver_300x250_sinoptik\"></div>\n" +
            "                        <div id=\"adriver_300x250_sinoptik1\"></div>\n" +
            "                                        <div id=\"adsZone\"></div>\n" +
            "                                            <div style=\"position:absolute;visibility:hidden;height:0;\"><iframe id=\"ar_container_2\" width=1 height=1 marginwidth=0 marginheight=0 scrolling=no frameborder=0></iframe></div><div id=\"ad_ph_2\" class=\"banerBlock1\"></div>\n" +
            "                                        <!-- AdSense_300x250 -->\n" +
            "                    <div id=\"sin_300x250_cont_bott\"></div>\n" +
            "                                                    <div class=\"partnerLink\">\n" +
            "                        <a onclick=\"_gaq.push(['_trackEvent', 'Партнеры ВК', 'http://vk.com/vkramatorske']);\" class=\"pVk\" href=\"http://vk.com/vkramatorske\" target=\"_blank\" rel=\"nofollow\">Все о Краматорске и немного больше</a>\n" +
            "                    </div>\n" +
            "                                                                                <div id=\"sinoptik_bot_line\"></div>\n" +
            "                        <!--/noindex-->\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    <div id=\"footer\">\n" +
            "            <div id=\"b-treasures\" class=\"clearfix\">\n" +
            "        <div style=\"background-image: url(//sinst.fwdcdn.com/img/newImg/tr1.jpg);\" class=\"b-treasures-item b-treasures-first\">\n" +
            "            <a class=\"b-treasures-link\" href=\"//sinoptik.ua/погода-на-дорогах\">\n" +
            "                Погода на дорогах            </a>\n" +
            "            Вам предстоит поездка в ближайшие дни? Узнайте, какая погода ожидает вас по всему следованию маршрута...        </div>\n" +
            "        <div style=\"background-image: url(//sinst.fwdcdn.com/img/newImg/tr2.jpg);\" class=\"b-treasures-item\">\n" +
            "            <a class=\"b-treasures-link\" href=\"//sinoptik.ua/горы/карпаты\">\n" +
            "                Погода в <span>Украинских Карпатах</span>            </a>\n" +
            "            Погода в более чем 40 горнолыжных курортах и здравницах Карпат        </div>\n" +
            "        <div style=\"background-image: url(//sinst.fwdcdn.com/img/newImg/tr3.jpg);\" class=\"b-treasures-item\">\n" +
            "            <a class=\"b-treasures-link\" href=\"//sinoptik.ua/море/средиземное\">\n" +
            "                Погода на курортах Средиземного моря            </a>\n" +
            "            Погода на курортах Франции, Италии, Греции, Кипра, Турции, Черногории, Египта...        </div>\n" +
            "        <div style=\"background-image: url(//sinst.fwdcdn.com/img/newImg/tr4.jpg);\" class=\"b-treasures-item\">\n" +
            "            <a class=\"b-treasures-link\" href=\"//sinoptik.ua/море/черное\">\n" +
            "                Погода в Крыму и других курортах Черного моря            </a>\n" +
            "            Температура воздуха и воды, облачность и осадки в прибрежных городах Украины, Турции, Болгарии...        </div>\n" +
            "    </div>\n" +
            "\n" +
            "        <div class=\"bMenu\">\n" +
            "  <div class=\"city\">Погода в Краматорске (\n" +
            "    <a href=\"//sinoptik.ua/украина\">Украина</a>\n" +
            "          &gt; <a href=\"//sinoptik.ua/украина/донецкая-область\">Донецкая область</a>\n" +
            "            )\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "        <div id=\"bfoot\">\n" +
            "            <div id=\"infoTime\">Обновлено 25.02.2015 в 13:11 (GMT+2)</div>\n" +
            "            <a href=\"//sinoptik.ua/\">Погода</a> во всех уголках Украины, прогноз погоды от SINOPTIK        </div>\n" +
            "    </div>\n" +
            "    <div id=\"copyright\">\n" +
            "        Все права принадлежат &copy; 2010-2015 sinoptik.ua. Партнеры проекта: Украинский Гидрометцентр, <img width='56' height='12' alt='FORECA' src='//sinst.fwdcdn.com/img/partners/foreca_logo.png' class='forecaLogo' />.<br/> <a href='//www.ukr.net/ru/terms/' rel='nofollow' target='_blank'>Соглашение о конфиденциальности</a> <a href='//sinoptik.ua/соглашение' rel='nofollow' target='_blank'>Пользовательское соглашение</a> <a href='javascript:;' onclick='feedback_show(\"feedback\");'>Написать нам</a> <a href='javascript:;' onclick='feedback_show(\"ad\");'>Реклама</a>        <div id=\"bigmirTop\" style=\"text-align:center;\"><a rel=\"nofollow\" href=\"http://www.bigmir.net/\" target=\"_blank\" style=\"color:#0000ab;text-decoration:none;font:10px Tahoma;\">bigmir<span style=\"color:#ff0000;\">)</span>net<br/>TOP 100</a></div>\n" +
            "        <a class=\"liveinternet\" href=\"//www.liveinternet.ru/click\" target=_blank><img alt=\"LiveInternet\" width=\"31\" height=\"31\" src=\"//sinst.fwdcdn.com/img/newImg/liveinternet.gif\"></a>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div id=\"tooltipS\"><div class=\"tooltip-tip-content\"></div></div>\n" +
            "\n" +
            "<script type=\"text/javascript\">\n" +
            "    var _gaq =_gaq||[], SIN = SIN || {};\n" +
            "    SIN.globals = SIN.globals || {};\n" +
            "    SIN.globals._gaId = 'UA-5903420-6';\n" +
            "    SIN.globals.SITE_URI = '//sinoptik.ua/';\n" +
            "    SIN.globals.STATIC_DOMAIN = '//sinst.fwdcdn.com/';\n" +
            "    SIN.globals.SITE_LANG = 'ru';\n" +
            "    SIN.globals.COOKIE_DOMAIN = '.sinoptik.ua';\n" +
            "</script>\n" +
            "<script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/3/lang_ua.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/1/jquery-1.10.0.min.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/10/ac.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/1/jquery.utils.js\"></script>\n" +
            "<script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/80/common.js\"></script>\n" +
            "<script type=\"text/javascript\">\n" +
            "    (function(){\n" +
            "        _gaq.push(['_setAccount',SIN.globals._gaId]);\n" +
            "        SIN.utility.load(\"//www.google-analytics.com/ga.js\")\n" +
            "    })();\n" +
            "</script>\n" +
            "<!--bigmir)net TOP 100 Part 1-->\n" +
            "<span id='obHP'></span>\n" +
            "<script type=\"text/javascript\"><!--\n" +
            "    bmN=navigator,bmD=document,bmD.cookie='b=b',i=0,bs=[],bm={o:1,v:16864144,s:16864144,t:6,c:bmD.cookie?1:0,n:Math.round((Math.random()* 1000000)),w:0};\n" +
            "    try{obHP.style.behavior=\"url('#default#homePage')\";obHP.addBehavior('#default#homePage');if(obHP.isHomePage(window.location.href))bm.h=1;}catch(e){};\n" +
            "    for(var f=self;f!=f.parent;f=f.parent)bm.w++;\n" +
            "    try{if(bmN.plugins&&bmN.mimeTypes.length&&(x=bmN.plugins['Shockwave Flash']))bm.m=parseInt(x.description.replace(/([a-zA-Z]|\\s)+/,''));\n" +
            "    else for(var f=3;f<20;f++)if(eval('new ActiveXObject(\"ShockwaveFlash.ShockwaveFlash.'+f+'\")'))bm.m=f}catch(e){}\n" +
            "    try{bm.y=bmN.javaEnabled()?1:0}catch(e){}\n" +
            "    try{bmS=screen;bm.v^=bm.d=bmS.colorDepth||bmS.pixelDepth;bm.v^=bm.r=bmS.width}catch(e){}\n" +
            "    r=bmD.referrer.slice(7);if(r&&r.split('/')[0]!=window.location.host){bm.f=escape(r);bm.v^=r.length}\n" +
            "    bm.v^=window.location.href.length;for(var x in bm) if(/^[ohvstcnwmydrf]$/.test(x)) bs[i++]=x+bm[x];\n" +
            "    //-->\n" +
            "</script>\n" +
            "<noscript><img src=\"//c.bigmir.net/?v16864144&s16864144&t6\" width=\"0\" height=\"0\" alt=\"\" title=\"\" border=\"0\" /></noscript>\n" +
            "<!--bigmir)net TOP 100 Part 1-->\n" +
            "<!-- COUNTER -->\n" +
            "<script type=\"text/javascript\">//<!--\n" +
            "    var d=document,n=navigator,s=screen;d.cookie=\"co=1\";\n" +
            "    SIN.utility.load('https://counter.ukr.net/weather/cnt.php?data=303012261,15,208&rand='+Math.random()+'&r='+escape(d.referrer)+'&p='+escape(window.location.href)+'&c='+(d.cookie?'y':'n')+'&fr='+(self!=top?'y':'n')+'&tz='+(new Date()).getTimezoneOffset()+'&j='+(n.javaEnabled()?'y':'n')+'&s='+s.width+'*'+s.height+'&d='+(s.colorDepth?s.colorDepth:s.pixelDepth)+'&js=y');\n" +
            "    //--></script>\n" +
            "<noscript><img src=\"https://counter.ukr.net/weather/cnt.php?js=n\" alt=\"NetPromoter - регистрация сайта в поисковиках | продвижение сайта | раскрутка сайта\" style=\"width:1px;height:1px;border:none;\"/></noscript>\n" +
            "<!-- /COUNTER -->\n" +
            "<!-- Yandex.Metrika counter -->\n" +
            "<script type=\"text/javascript\">\n" +
            "    (function (d, w, c) {\n" +
            "        (w[c] = w[c] || []).push(function() {\n" +
            "            try {\n" +
            "                w.yaCounter24146905 = new Ya.Metrika({id:24146905,\n" +
            "                    webvisor:true,\n" +
            "                    clickmap:true,\n" +
            "                    accurateTrackBounce:true,\n" +
            "                    ut:\"noindex\"});\n" +
            "            } catch(e) { }\n" +
            "        });\n" +
            "\n" +
            "        var n = d.getElementsByTagName(\"script\")[0],\n" +
            "            s = d.createElement(\"script\"),\n" +
            "            f = function () { n.parentNode.insertBefore(s, n); };\n" +
            "        s.type = \"text/javascript\";\n" +
            "        s.async = true;\n" +
            "        s.src = (d.location.protocol == \"https:\" ? \"https:\" : \"http:\") + \"//mc.yandex.ru/metrika/watch.js\";\n" +
            "\n" +
            "        if (w.opera == \"[object Opera]\") {\n" +
            "            d.addEventListener(\"DOMContentLoaded\", f, false);\n" +
            "        } else { f(); }\n" +
            "    })(document, window, \"yandex_metrika_callbacks\");\n" +
            "</script>\n" +
            "<noscript><div><img src=\"//mc.yandex.ru/watch/24146905?ut=noindex\" style=\"position:absolute; left:-9999px;\" alt=\"\" /></div></noscript>\n" +
            "<!-- /Yandex.Metrika counter -->\n" +
            "\n" +
            "\n" +
            "    <noscript><img src=\"//ua.adriver.ru/cgi-bin/rle.cgi?sid=141297&bn=1&bt=21&pz=1&rnd=258290485\" border=0 width=1 height=1><img src=\"//ua.adriver.ru/cgi-bin/rle.cgi?sid=141297&bn=6&bt=21&pz=3&rnd=609686989\" border=0 width=1 height=1></noscript>\n" +
            "    <script type=\"text/javascript\" src=\"//sinst.fwdcdn.com/js/1/adriver.min.js\"></script>\n" +
            "\n" +
            "    <script type=\"text/javascript\">\n" +
            "        $(window).on('load', function(){\n" +
            "            var rand = Math.round(Math.random()*999999999), tail=escape(document.referrer||'unknown');\n" +
            "            _gaq.push(['_trackEvent', 'Просмотры по странам', 'Погода в городе', 'ukraine']);            SIN.utility.load(\"//target.ukr.net/?trunc=1&cb=onGeoData&get=70,71,72,100,101&set=YGVoMCAnOiUwbmBkaCA+JzQ8OzBuYGdoPic0ODQhOicmPg--\");\n" +
            "                        SIN.AdsLoader.addOldAds({ left:{zone1 : [{id:25, size:3, counter:1188, cost:0, partner_url:'aukro.ua', bgImg:'//sinst.fwdcdn.com/_uploaded_files/ads/25/bg.3.jpg', stylespec:'#leftCol .custom25 {width:423px;height:160px;padding: 17px 0 5px 248px!important;border:1px solid #ccc;}#leftCol .custom25 .link {width: 130px!important;margin-left: 7px;height: 145px;text-align: center;}#leftCol .custom25 .img {margin: 0 0 7px;float: none;}#leftCol .custom25 .text {font:12px/12px arial; padding: 0 3px; max-height: 37px; color: #0280fb;}#leftCol .custom25 .partnerName {display: none;}', items:[{title:'Те&shy;ле&shy;фо&shy;ны от 210 грн!', url:'http://aukro.ua/telefony-smartfony-drugie-modeli-58352?listing_sel=2&listing_interval=7&offer_type=1&city=&order=bd&price_from=&price_to=&string=&view=gal&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_mobile_tel', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/04/A4/uft_273a7fbb428b8fbc334e53547f50735e.jpg', w:100, h:100}, {title:'Iphone от 1200грн! Здесь де&shy;шев&shy;ле!', url:'http://aukro.ua/telefony-smartfony-apple-iphone-107160?listing_sel=2&listing_interval=7&offer_type=1&city=&order=bd&price_from=&price_to=&string=&view=gal&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_mobile_iphone2', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/F6/1D/uft_769df8c3b5ade20062b403437a27452f.jpg', w:100, h:100}, {title:'Обувь от 150грн!', url:'http://aukro.ua/zhenskaya-obuv-52312?a_text_f[10743][1]=&listing_sel=2&listing_interval=7&offer_type=1&city=&order=bd&price_from=&price_to=&string=&view=gal&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_moda_obuv', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/3F/4F/uft_0bdb507659058d6c037715465ddf4369.jpg', w:100, h:100}, {title:'Аудио&shy;тех&shy;ни&shy;ка', url:'http://aukro.ua/audio-radio?order=qd&offerTypeBuyNow=1&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_tech_audio', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/28/02/uft_595ee7199f3653921605cf0cedd6b2c0.jpg', w:100, h:100}, {title:'Муж&shy;ская обувь от 250грн!', url:'http://aukro.ua/muzhskaya-obuv-58037?a_text_f[11208][1]=&listing_sel=2&listing_interval=7&offer_type=1&city=&order=bd&price_from=&price_to=&string=&view=gal&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_moda_mobuv', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/82/27/uft_debbb317c5b83f2e41cce6244111a750.jpg', w:100, h:100}, {title:'Iphone от 1200грн! Здесь де&shy;шев&shy;ле!', url:'http://aukro.ua/telefony-smartfony-apple-iphone-107160?listing_sel=2&listing_interval=7&offer_type=1&city=&order=bd&price_from=&price_to=&string=&view=gal&ap=1&aid=22362818&utm_source=sinoptik.ua&utm_medium=advert&utm_campaign=blok_mobile_iphone2', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/25/11/A0/uft_3c5689bd66ad816717db8a37243da276.jpg', w:100, h:100}]}],zone8 : []}, right:{zone7 : [{id:118, size:1, counter:3193, cost:1, partner_url:'dex.ua', hImg:'//sinst.fwdcdn.com/_uploaded_files/ads/0/head.14.png', items:[{title:'Скид&shy;ки до50% на бы&shy;то&shy;вую тех&shy;ни&shy;ку! Мощ&shy;ный утюг с ан&shy;ти&shy;при&shy;гар&shy;ной по&shy;дош&shy;вой', url:'http://dex.ua/novosti/206-18-02-15?utm_source=sinoptik.ua&utm_medium=informer_cpc&utm_content=utyugi&utm_campaign=skidki50wave2_february', cost:'199 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/118/21/B4/uft_e47db03215e6af70b68ad7f6fcf4ace7.jpg', w:100, h:100}, {title:'Скид&shy;ки до50% на бы&shy;то&shy;вую тех&shy;ни&shy;ку! Элек&shy;тро&shy;чай&shy;ник из нерж/ста&shy;ли, под&shy;свет&shy;ка, Strix', url:'http://dex.ua/novosti/206-18-02-15?utm_source=sinoptik.ua&utm_medium=informer_cpc&utm_content=chajniki&utm_campaign=skidki50wave2_february', cost:'541 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/118/64/A2/uft_02d79d9dfa2c977908a40d799996631c.jpg', w:100, h:100}, {title:'Скид&shy;ки до50% на бы&shy;то&shy;вую тех&shy;ни&shy;ку! DMC55: 10 про&shy;грамм, 5л, по&shy;кры&shy;тие ча&shy;ши DAIKIN', url:'http://dex.ua/novosti/206-18-02-15?utm_source=sinoptik.ua&utm_medium=informer_cpc&utm_content=multivarki&utm_campaign=skidki50wave2_february', cost:'998 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/118/53/B8/uft_8680883aa18682b3c9aa9b6856e7e39f.jpg', w:100, h:100}]}, {id:213, size:1, counter:1834, cost:0, partner_url:'inchofgold.com.ua', hImg:'//sinst.fwdcdn.com/_uploaded_files/ads/0/head.21.jpg', items:[{title:'Рос&shy;кош&shy;ное коль&shy;цо в по&shy;зо&shy;ло&shy;те 750 про&shy;бы', url:'http://inchofgold.com.ua/shop/dlya-zhenwin/kolca/?utm_source=sinoptik&utm_medium=cpc&utm_campaign=KG252_12_02_2015', cost:'349 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/213/BD/B9/uft_5f7d3862f8ac61c975119d83ea9b9da6.jpg', w:100, h:100}, {title:'Экс&shy;клю&shy;зив&shy;ный брас&shy;лет. Элит&shy;ная би&shy;жу&shy;те&shy;рия', url:'http://inchofgold.com.ua/shop/dlya-zhenwin/braslety/?utm_source=sinoptik&utm_medium=cpc&utm_campaign=713_12_02_2015', cost:'290 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/213/F4/85/uft_c5acd4c7c347b4be2d162c8ab705c153.jpg', w:100, h:100}, {title:'Экс&shy;клю&shy;зив&shy;ный брас&shy;лет. Элит&shy;ная би&shy;жу&shy;те&shy;рия', url:'http://inchofgold.com.ua/shop/dlya-zhenwin/braslety/?utm_source=sinoptik&utm_medium=cpc&utm_campaign=36A_12_02_2015', cost:'290 грн', img:'//sinst.fwdcdn.com/_uploaded_files/ads/213/76/6C/uft_97819f950fca670d291b1267f0ad9a45.jpg', w:100, h:100}]}, {id:59, size:1, counter:1841, cost:1, partner_url:'eshko.ua', hImg:'//sinst.fwdcdn.com/_uploaded_files/ads/59/head.14.png', items:[{title:'Вре&shy;мя учить ино&shy;стран&shy;ный! 11 ос&shy;нов&shy;ных язы&shy;ков. Ска&shy;чай бес&shy;плат&shy;ный урок!', url:'http://ad.adriver.ru/cgi-bin/click.cgi?sid=1&ad=408879&bt=21&pid=1317360&bid=2841045&bn=2841045&rnd=' + Math.round(Math.random() * 100000) + '', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/59/F9/0B/uft_e26aa353d2c0fbcbd3d8aae00fecd3c8.jpg', w:100, h:100}, {title:'83 кур&shy;са – 83 бес&shy;плат&shy;ных проб&shy;ных уро&shy;ка. По&shy;про&shy;буй, ска&shy;чай бес&shy;плат&shy;но!', url:'http://ad.adriver.ru/cgi-bin/click.cgi?sid=1&ad=408879&bt=21&pid=1213677&bid=2674512&bn=2674512&rnd=' + Math.round(Math.random() * 100000) + '', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/59/B6/DA/uft_1272a42954acf0c7f0052701f9adddc0.jpg', w:100, h:100}, {title:'Вре&shy;мя учить Ис&shy;пан&shy;ский! Ска&shy;чай бес&shy;плат&shy;ный урок и учи язык до&shy;ма!', url:'http://ad.adriver.ru/cgi-bin/click.cgi?sid=1&ad=408879&bt=21&pid=1213678&bid=2674514&bn=2674514&rnd=' + Math.round(Math.random() * 100000) + '', cost:'', img:'//sinst.fwdcdn.com/_uploaded_files/ads/59/34/35/uft_3da4163bc1ccbbd54b50bc5f903ca63c.jpg', w:100, h:100}]}, {id:94, size:1, counter:2998, cost:0, partner_url:'1palladium.com.ua', hImg:'//sinst.fwdcdn.com/_uploaded_files/ads/94/head.2.jpg', items:[{title:'Aeg RA 5522', url:'http://1palladium.com.ua/aeg_ra_5522.html', cost:'1199 грн.', img:'//sinst.fwdcdn.com/_uploaded_files/ads/94/EE/64/uft_1f09f475e030e2c934819c610c405a51.jpg', w:100, h:100}, {title:'ACER E5-511-P1HX (NX.MNYEU.006) Black', url:'http://1palladium.com.ua/acer_e5-511-p1hx_nx_mnyeu_006_black.html', cost:'12622 грн.', img:'//sinst.fwdcdn.com/_uploaded_files/ads/94/25/6B/uft_c1b413234c99a768963fb4b5cc4179cd.jpg', w:100, h:100}, {title:'Gorenje TGR 100 SN', url:'http://1palladium.com.ua/gorenje_tgr_100_sn.html', cost:'2593 грн.', img:'//sinst.fwdcdn.com/_uploaded_files/ads/94/A6/09/uft_2063ae9bcf45a3e4e34f797f12341a64.jpg', w:100, h:100}]}]}, top:{zone9 : [{id:183, size:1, counter:1, cost:0, partner_url:'http://ad.adriver.ru/cgi-bin/click.cgi?sid=1&amp;ad=506514&amp;bt=21&amp;pid=1851528&amp;bid=3700624&amp;bn=3700624&amp;rnd=1183901894', bgImg:'//sinst.fwdcdn.com/_uploaded_files/ads/183/bg.1.jpg', items:[]}]}, right_rotate :false });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='GNM2177' style='text-align:center;display:none;margin:0 4px 20px' ></div>\",\n" +
            "                url: \"//sslg.novostimira.biz/l/2177?v=\"+Math.floor((new Date()).getTime()/(1000*600)),\n" +
            "                zone: \"left\",\n" +
            "                slot: \"slot1\",\n" +
            "                percent: 27,\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='bn_Vw4h1DquRJ' style='margin:0 4px 20px'></div>\",\n" +
            "                url: \"//recreativ.ru/rcode.Vw4h1DquRJ.js\",\n" +
            "                zone: \"left\",\n" +
            "                slot: \"slot2\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div style=\\\"background:url('//tizerclik.com/images/100x15.png') no-repeat 0 0;height:15px;width:100px;margin-left:8px;\\\"></div><div id='teaser_171' style='margin:0 4px 20px'></div>\",\n" +
            "                url: \"//tizerclik.com/show/?block_id=171&r='\" + tail + \"&\" + rand,\n" +
            "                zone: \"left\",\n" +
            "                slot: \"slot2\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='trafmag_cbfeeb0a'></div>\",\n" +
            "                url: \"//trafmag.com/bannercode-cbfeeb0a.js\",\n" +
            "                zone: \"left\",\n" +
            "                slot: \"slot4\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='trafmag_4e23d8e1'>&nbsp;</div>\",\n" +
            "                url: \"//trafmag.com/bannercode-4e23d8e1.js\",\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot1\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='mk'></div>\",\n" +
            "                url: \"//makeup.com.ua/informer/sinoptik/mk/script.js\",\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot2\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='GNM2156' style='text-align:center;display:none'></div>\",\n" +
            "                url: \"//sslg.novostimira.biz/l/2156?v=\"+Math.floor((new Date()).getTime()/(1000*600)),\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot3\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div style=\\\"background:url('//tizerclik.com/images/logo_grey2.png') no-repeat 0 0;height:29px;width:300px;\\\"></div><div id='teaser_33' style='margin-bottom:15px'></div>\",\n" +
            "                url: \"//tizerclik.com/show/?block_id=33&r=\"+tail+\"&\"+rand,\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot4\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='admixer673_168' class='admixer1'></div>\",\n" +
            "                url: \"//admixermonitorstorage.blob.core.windows.net/sinoptik-ua/673x168/673x168.js\",\n" +
            "                cb: function (){\n" +
            "                    window.admixZArr = (window.admixZArr || []);\n" +
            "                    window.admixZArr.push({ z: 'b9ede27d-7d11-425f-a1a1-6613468d8fa4', ph: 'admixer673_168', maxItemsCount: 4, loadCallback: function (slot) { slot.customTeasersRender = true; }, renderedCallback: function (slot) { renderTeasers673_168(slot.data, slot.phId); } });\n" +
            "                },\n" +
            "                zone: \"left\",\n" +
            "                slot: \"slot2\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='bn_P9mw76syyt'></div>\",\n" +
            "                url: \"//recreativ.ru/rcode.P9mw76syyt.js\",\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot5\",\n" +
            "                src: \"external\"\n" +
            "            });\n" +
            "            SIN.AdsLoader.addAds({\n" +
            "                block: \"<div id='yottosBlock' style='margin-bottom:15px'></div>\",\n" +
            "                url: \"//cdn.yottos.com/getmyad/b6c0763c-00f5-11e3-96e7-00e081bad801_a.js\",\n" +
            "                zone: \"right\",\n" +
            "                slot: \"slot5\",\n" +
            "                src: \"external\",\n" +
            "                globals: {\n" +
            "                    yottos_advertise: \"b6c0763c-00f5-11e3-96e7-00e081bad801\",\n" +
            "                    yottos_advertise_div_display: \"yottosBlock\"\n" +
            "                }\n" +
            "            });\n" +
            "            SIN.AdsLoader.loadAds();\n" +
            "\n" +
            "            SIN.utility.load(\"//ua.adriver.ru/cgi-bin/rle.cgi?sid=141297&bn=1&bt=21&pz=1&rnd=\"+rand+\"&tail256=\"+tail);\n" +
            "            SIN.utility.load(\"//ua.adriver.ru/cgi-bin/rle.cgi?sid=141297&bn=6&bt=21&pz=3&rnd=\"+rand+\"&tail256=\"+tail);\n" +
            "\n" +
            "            var w = window;\n" +
            "            w.admixZArr = (w.admixZArr || []);\n" +
            "            w.admixerSmOptions = (w.admixerSmOptions || {});\n" +
            "            w.admixerSmOptions.showAdsOnLoad = true;\n" +
            "            w.admixerSmOptions.cdnPath = location.protocol + '//admixercreatives.blob.core.windows.net';\n" +
            "            w.admixerSmOptions.cdnFP = location.protocol + '//admixercreatives.blob.core.windows.net';\n" +
            "            if (!w.admixerSm) { SIN.utility.load(\"//admixercreatives.blob.core.windows.net/scriptlibsecure/scriptlib/asm2.js?v=3\"); }\n" +
            "                                    w.admixZArr.push({ z: '36c0efe9-0527-49f9-b160-dbd7583a3d3b', ph: 'admixer_async_2027083', loadCallback: onAdmixBannerLoaded673});\n" +
            "            function onAdmixBannerLoaded673(slotData) {if(slotData.items.length){$('#admixer_async_2027083').addClass('adr_b')}else{loadForAdmixer673.loadFnc();}}\n" +
            "                                    var li=new Image();li.style.width=0;li.style.position='absolute';li.src=\"//counter.yadro.ru/hit?t44.11;r\"+escape(document.referrer)+((typeof(screen)==\"undefined\")?\"\":\";s\"+screen.width+\"*\"+screen.height+\"*\"+(screen.colorDepth?screen.colorDepth:screen.pixelDepth))+\";u\"+escape(document.URL)+\";\"+Math.random();document.body.appendChild(li);\n" +
            "            SIN.utility.load(\"//c.bigmir.net/?\"+bs.join('&'));\n" +
            "            \n" +
            "                    });\n" +
            "        function onGeoData(data) {\n" +
            "            var ri=0,ci=0,ct=1,skipCities={},serveTo=[],custom=[],showAPL=false,citiesId=SIN.utility.cookie('cities')||'';\n" +
            "            for(;ri<data.g.regions.length;ri++){var regId=data.g.regions[ri].id;if(regId==1||regId==3||regId==11||regId==15){showAPL=true;}var cities=(data.g.regions[ri].cities);for(;ci<cities.length;ci++){if(!skipCities[cities[ci].id])serveTo.push(cities[ci].id);}}\n" +
            "            for(;ct<=101;ct++){if(data.c[ct])custom[ct]=data.c[ct];}\n" +
            "            if(serveTo.length){for(var c=0;c<serveTo.length;c++){custom[80+c]=serveTo[c];}}else{custom[80]=\"noneGD\";}\n" +
            "            custom[50] = 'europe';\n" +
            "            custom[51] = 'ukraine';\n" +
            "            custom[52] = 'kramatorsk';\n" +
            "            function getStd(){for(var i=0,j,s=[];i<custom.length;i++){if(custom[i])s.push(i+'='+escape(custom[i]))}return s.length?'&custom='+escape(s.join(';')):''};\n" +
            "            new adriver(\"adriver_728x90_sinoptik\", {sid:141297,bt:52,pz:1,bn:1,keyword:encodeURIComponent(citiesId),custom: custom});                                                            loadForAdmixer.setParams({sid:141297,bt:52,pz:2,bn:2, keyword:encodeURIComponent(citiesId),custom: custom});\n" +
            "            var adrBlock = new adriver(\"adriver_300x250_sinoptik\",{sid:141297,bt:52,pz:2,bn:8, keyword:encodeURIComponent(citiesId),custom: custom});\n" +
            "            adrBlock.onLoadComplete(function(){\n" +
            "                if (!this.reply.adid || this.reply.adid == 283742) {\n" +
            "                    window.admixZArr=(window.admixZArr||[]);\n" +
            "                    window.admixZArr.push({z:'fa7d1659-65cb-4909-acce-e7bf119e95b4',ph:'admixer_async_657326783',loadCallback:onAdmixBannerLoaded});\n" +
            "                }\n" +
            "            });\n" +
            "            function onAdmixBannerLoaded(slotData){if(slotData.items.length){$('#admixer_async_657326783').addClass('adr_b')}else{loadForAdmixer.loadFnc(\"adriver_300x250_sinoptik1\")}}\n" +
            "                        adriverPoster(\"//ua.adriver.ru/cgi-bin/erle.cgi?sid=141297&bn=3&target=blank&bt=49&pz=2\"+getStd(),2);                        loadForAdmixer673.setStd(getStd());            new adriver(\"sinoptik_bot_line\", {sid:141297,bt:52,pz:3,bn:5,keyword:encodeURIComponent(citiesId),custom: custom});\n" +
            "            new adriver(\"adriver_210x55_sinoptik\", {sid:141297,bt:52,pz:2,bn:5,keyword:encodeURIComponent(citiesId),custom: custom});\n" +
            "            new adriver(\"sin_300x250_cont_bott\", {sid:141297, bt:52, bn:13, pz:2});\n" +
            "                    }\n" +
            "                var loadForAdmixer={setParams:function(prm){this.prm=prm||{}},loadFnc:function(id){new adriver(id,this.prm)}};\n" +
            "        var loadForAdmixer673={setStd:function(std){this.std=std||''},loadFnc:function(){adriverPoster(\"//ua.adriver.ru/cgi-bin/erle.cgi?sid=141297&bn=3&target=blank&bt=49&pz=3\"+this.std,1);}};\n" +
            "        function adriverPoster(L,arcn){\n" +
            "            var W=window,D=document,E=D.documentElement,T=0,N=arcn||1,P=0,C=D.compatMode==\"CSS1Compat\",\n" +
            "                X='<scr'+'ipt type=\"text/javascript\">var ar_bnum='+N+';setTimeout(function(e){if(!self.CgiHref){document.close();e=parent.document.getElementById(\"ar_container_\"+ar_bnum);e.parentNode.removeChild(e)}},3000);',\n" +
            "                Y='<\\/sc'+'ript><sc'+'ript type=\"text/javascript\" src=\"'+L+'&tail256='+escape(D.referrer||'unknown')+'&rnd='+Math.round(Math.random()*999999999)+'\"><\\/sc'+'ript>';\n" +
            "            function G(){if(T++<100){var o=D.getElementById('ar_container_'+N);if(o){try{var d=o.contentDocument||(W.ActiveXObject&&W.frames['ar_container_'+N].document);if(d){d.write(X+Y)}else setTimeout(arguments.callee,100)}catch(e){try{o.src = \"javascript:{document.write('\"+X+'document.domain=\"'+D.domain+'\";'+Y+\"')}\";return}catch(E){}}}else setTimeout(arguments.callee,100)}}\n" +
            "            function A(e,t,f){if(e.addEventListener)e.addEventListener(t,f,false);else if(e.attachEvent)e.attachEvent('on'+t,f)}\n" +
            "            function R(e,t,f){if(e.removeEventListener)e.removeEventListener(t,f,false);else if(e.detachEvent)e.detachEvent('on'+t,f)}\n" +
            "            function S(){var ch=self.innerHeight||C&&E.clientHeight||D.body.clientHeight,st=self.pageYOffset||C&&E.scrollTop||D.body.scrollTop;if(P>=st&&st+ch>=P){R(W,'scroll',S);G()}}\n" +
            "            var o=D.getElementById('ad_ph_'+N);if(o){while(o.offsetParent){P+=o.offsetTop;o=o.offsetParent}A(W,'scroll',S);S()};\n" +
            "        }\n" +
            "            </script>\n" +
            "</body>\n" +
            "</html>";

    private DataParser parser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        parser = DataParser.getInstance();
    }

    public void testParserHTMLHome() {
        WeatherStruct weather = parser.parserHTML(site);

        assertNotNull(weather.getWeatherMonday());
        assertNotNull(weather.getWeatherTuesday());
        assertNotNull(weather.getWeatherWednesday());
        assertNotNull(weather.getWeatherThursday());
        assertNotNull(weather.getWeatherFriday());
        assertNotNull(weather.getWeatherSaturday());
        assertNotNull(weather.getWeatherSunday());
        assertNotNull(weather.getWindDescription());

        assertEquals(weather.getAllWeathers().size(), 7);
        assertEquals(weather.getWarningWind(), true);
    }

    public void testParseDetailInfo() {
        ArrayList<ItemDetail> details = DataParser.parseDetailInfo(Jsoup.parse(site));
        assertNotNull(details);
        assertEquals(details.size(), 4);
        assertEquals(details.get(0).chanceOfPrecipitation.get(0), "-");
        assertEquals(details.get(0).dayStage, "ночь");
        assertEquals(details.get(0).dayTime.get(0), "2:00");
        assertEquals(details.get(0).humidity.get(0), "69");
        assertEquals(details.get(0).imageWeather.get(0), "https://sinst.fwdcdn.com/img/weatherImg/s/n000.gif");
        assertEquals(details.get(0).pressure.get(0), "758");
        assertEquals(details.get(0).temperatureFell.get(0), Html.fromHtml("-3&deg").toString());
        assertEquals(details.get(0).temperature.get(0), Html.fromHtml("0&deg").toString());
    }
}
