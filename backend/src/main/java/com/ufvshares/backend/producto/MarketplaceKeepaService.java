package com.ufvshares.backend.producto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class MarketplaceKeepaService {

  private static final Pattern ASIN_PATTERN = Pattern.compile("^[A-Z0-9]{10}$");
  private static final String USER_AGENT = "Mozilla/5.0";
  private static final Pattern PRICE_JSON_PATTERN = Pattern.compile("\"price\"\\s*:\\s*\"?([0-9]+(?:[\\.,][0-9]{1,2})?)\"?");

  private record AsinCandidate(String asin, int score) {}

  public Map<String, Object> getMarketplaceData(Producto producto) {
    String searchQuery = buildSearchQuery(producto);

    Map<String, Object> result = new LinkedHashMap<>();
    result.put("source", "AMAZON_SCRAPING");
    result.put("query", searchQuery);
    result.put("amazonSearchUrl", "https://www.amazon.es/s?k=" + urlEncode(searchQuery));
    result.put("keepaSearchUrl", "https://keepa.com/#!search/" + urlEncode(searchQuery));
    result.put("sellerPrice", producto.getPrecio());

    try {
      List<String> asinCandidates = resolveAsinCandidatesFromAmazonSearch(searchQuery, 8);
      if (asinCandidates.isEmpty()) {
        result.put("available", false);
        result.put("message", "No se encontró ASIN equivalente para este producto");
        return result;
      }

      String selectedAsin = asinCandidates.get(0);

      result.put("available", true);
      result.put("asin", selectedAsin);
      result.put("amazonProductUrl", "https://www.amazon.es/dp/" + selectedAsin);
      result.put("keepaProductUrl", "https://keepa.com/#!product/3-" + selectedAsin);

      BigDecimal currentPrice = resolveCurrentAmazonPrice(selectedAsin, searchQuery);
      if (currentPrice != null) {
        result.put("currentOnlinePrice", currentPrice);
      }
    } catch (Exception ex) {
      result.put("available", false);
      result.put("message", "No se pudo consultar Amazon en este momento");
    }

    return result;
  }

  private String buildSearchQuery(Producto p) {
    String category = p.getCategoria() == null ? "" : p.getCategoria().name().replace('_', ' ');
    return (safe(p.getTitulo()) + " " + category).trim();
  }

  private List<String> resolveAsinCandidatesFromAmazonSearch(String query, int maxResults) {
    List<AsinCandidate> candidates = new ArrayList<>();
    Set<String> dedup = new LinkedHashSet<>();
    try {
      String searchUrl = "https://www.amazon.es/s?k=" + urlEncode(query);
      Document doc = Jsoup.connect(searchUrl)
          .userAgent(USER_AGENT)
          .timeout(10_000)
          .get();

      String[] queryTokens = query.toLowerCase(Locale.ROOT).split("[^a-z0-9áéíóúñü]+");
      for (Element item : doc.select("div.s-result-item[data-asin]")) {
        String asin = item.attr("data-asin").toUpperCase(Locale.ROOT).trim();
        if (!ASIN_PATTERN.matcher(asin).matches()) {
          continue;
        }
        if (!dedup.add(asin)) {
          continue;
        }

        String title = safe(item.selectFirst("h2 span") != null ? item.selectFirst("h2 span").text() : "")
            .toLowerCase(Locale.ROOT);
        int score = 0;
        for (String token : queryTokens) {
          if (token.length() < 3) continue;
          if (title.contains(token)) score++;
        }

        candidates.add(new AsinCandidate(asin, score));
      }
    } catch (Exception ignored) {
      return List.of();
    }

    candidates.sort(Comparator.comparingInt(AsinCandidate::score).reversed());
    List<String> top = new ArrayList<>();
    for (AsinCandidate candidate : candidates) {
      top.add(candidate.asin());
      if (top.size() >= maxResults) break;
    }
    return top;
  }

  private BigDecimal resolveCurrentAmazonPrice(String asin, String fallbackQuery) {
    BigDecimal direct = parsePriceFromAmazonProduct(asin);
    if (direct != null) return direct;
    BigDecimal byAsinInSearch = parsePriceFromAmazonSearchByAsin(asin, fallbackQuery);
    if (byAsinInSearch != null) return byAsinInSearch;
    return parsePriceFromAmazonSearch(fallbackQuery);
  }

  private BigDecimal parsePriceFromAmazonProduct(String asin) {
    try {
      String productUrl = "https://www.amazon.es/dp/" + urlEncode(asin);
      Document doc = Jsoup.connect(productUrl)
          .userAgent(USER_AGENT)
          .header("Accept-Language", "es-ES,es;q=0.9,en;q=0.8")
          .referrer("https://www.amazon.es/")
          .timeout(10_000)
          .get();

      String[] selectors = new String[] {
          "#corePrice_feature_div span.a-price span.a-offscreen",
          "span.a-price.aok-align-center span.a-offscreen",
          "#priceblock_ourprice",
          "#priceblock_dealprice",
          ".a-price .a-offscreen"
      };

      for (String selector : selectors) {
        Element priceEl = doc.selectFirst(selector);
        if (priceEl == null) continue;
        BigDecimal parsed = parseEuroPrice(priceEl.text());
        if (parsed != null) return parsed;
      }

      BigDecimal structured = parsePriceFromStructuredData(doc);
      if (structured != null) return structured;
    } catch (Exception ignored) {
      return null;
    }
    return null;
  }

  private BigDecimal parsePriceFromAmazonSearchByAsin(String asin, String query) {
    try {
      String searchUrl = "https://www.amazon.es/s?k=" + urlEncode(query);
      Document doc = Jsoup.connect(searchUrl)
          .userAgent(USER_AGENT)
          .header("Accept-Language", "es-ES,es;q=0.9,en;q=0.8")
          .referrer("https://www.amazon.es/")
          .timeout(10_000)
          .get();

      Element item = doc.selectFirst("div.s-result-item[data-asin='" + asin + "']");
      if (item == null) return null;

      String[] selectors = new String[] {
          "span.a-price span.a-offscreen",
          "span.a-price .a-price-whole",
          ".a-price .a-offscreen"
      };

      for (String selector : selectors) {
        Element priceEl = item.selectFirst(selector);
        if (priceEl == null) continue;
        String text = priceEl.text();
        if ("span.a-price .a-price-whole".equals(selector)) {
          Element fraction = item.selectFirst("span.a-price .a-price-fraction");
          if (fraction != null) {
            text = text + "," + fraction.text();
          }
        }
        BigDecimal parsed = parseEuroPrice(text);
        if (parsed != null) return parsed;
      }
    } catch (Exception ignored) {
      return null;
    }
    return null;
  }

  private BigDecimal parsePriceFromAmazonSearch(String query) {
    try {
      String searchUrl = "https://www.amazon.es/s?k=" + urlEncode(query);
      Document doc = Jsoup.connect(searchUrl)
          .userAgent(USER_AGENT)
          .header("Accept-Language", "es-ES,es;q=0.9,en;q=0.8")
          .referrer("https://www.amazon.es/")
          .timeout(10_000)
          .get();

      Element firstPrice = doc.selectFirst("div.s-result-item span.a-price span.a-offscreen");
      if (firstPrice == null) return null;
      return parseEuroPrice(firstPrice.text());
    } catch (Exception ignored) {
      return null;
    }
  }

  private BigDecimal parsePriceFromStructuredData(Document doc) {
    for (Element script : doc.select("script[type='application/ld+json']")) {
      String json = script.data();
      if (json == null || json.isBlank()) continue;
      Matcher matcher = PRICE_JSON_PATTERN.matcher(json);
      if (matcher.find()) {
        BigDecimal parsed = parseEuroPrice(matcher.group(1));
        if (parsed != null) return parsed;
      }
    }
    return null;
  }

  private BigDecimal parseEuroPrice(String text) {
    if (text == null || text.isBlank()) return null;
    String normalized = text
        .replace("€", "")
        .replaceAll("[^0-9,\\.]", "")
        .replace(".", "")
        .replace(',', '.');
    if (normalized.isBlank()) return null;
    try {
      return new BigDecimal(normalized).setScale(2, RoundingMode.HALF_UP);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private String urlEncode(String value) {
    return URLEncoder.encode(safe(value), StandardCharsets.UTF_8);
  }

  private String safe(String s) {
    return s == null ? "" : s;
  }
}