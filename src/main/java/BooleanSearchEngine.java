import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> indexingWords;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        indexingWords = new HashMap<>();
        File[] files = pdfsDir.listFiles();
        for (File pdf : files) {
            findPageEntryOnPDF(pdf);
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntries = indexingWords.get(word);
        Collections.sort(pageEntries);
        return pageEntries;
    }

    private Map<String, Integer> findUniqueWordsFreq(String[] words) {
        Map<String, Integer> freqs = new HashMap<>();
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
        }
        return freqs;
    }

    private void findPageEntryOnPage(Map<String, Integer> freqs, String fileName, int pageNumber) {
        for (String word : freqs.keySet()) {
            PageEntry pageEntry = new PageEntry(fileName, pageNumber, freqs.get(word));
            List<PageEntry> pageEntries;
            if (indexingWords.containsKey(word)) {
                pageEntries = indexingWords.get(word);
            } else {
                pageEntries = new ArrayList<>();
            }
            pageEntries.add(pageEntry);
            indexingWords.put(word, pageEntries);
        }
    }

    private void findPageEntryOnPDF(File pdf) throws IOException {
        var doc = new PdfDocument(new PdfReader(pdf));
        int countPages = doc.getNumberOfPages();
        for (int i = 1; i <= countPages; i++) {
            PdfPage page = doc.getPage(i);
            String text = PdfTextExtractor.getTextFromPage(page);
            String[] words = text.split("\\P{IsAlphabetic}+");
            Map<String, Integer> freqs = findUniqueWordsFreq(words);
            findPageEntryOnPage(freqs, pdf.getName(), i);
        }
    }
}

