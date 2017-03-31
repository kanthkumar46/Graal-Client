package edu.graal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.graal.GraalAlgorithm;
import edu.graal.ImmutablePDGGenerator;
import edu.graal.PDGGenerator;
import edu.graal.adapter.GraalAdapter;
import edu.graal.graphs.PDGraph;
import edu.graal.utils.GraalResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by KanthKumar on 3/29/17.
 */
@Controller
public class GraalController {
    private final PDGGenerator pdgGenerator = ImmutablePDGGenerator.of();
    private final GraalAlgorithm graalAlgorithm = new GraalAlgorithm();

    @Autowired
    private GraalAdapter graalAdapter;

    @ModelAttribute
    public void addDefault(Model model) {
        String code = "class Odd {\n" +
                "    public int countOdd(int[] input) {\n" +
                "        int oddCount = 0;\n" +
                "        for (int i = 0; i < input.length; i++) {\n" +
                "            if (input[i] % 2 != 0) {\n" +
                "                oddCount++;\n" +
                "            }\n" +
                "        }\n" +
                "        return oddCount;\n" +
                "    }\n" +
                "}";
        model.addAttribute("code" , code);
    }

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/pdg")
    public String createPDG(@RequestParam(value="program") String program, Model model) throws JsonProcessingException {
        PDGraph pdGraph = pdgGenerator.createPDG(program, 0);
        String json = graalAdapter.convertToJson(pdGraph);
        model.addAttribute("graph", json);
        return "index";
    }

    @RequestMapping("/align")
    public String alignPDG(@RequestParam(value="originalProgram") String originalProgram,
                           @RequestParam(value="suspectProgram") String suspectProgram,
                           Model model) throws JsonProcessingException {
        PDGraph original = pdgGenerator.createPDG(originalProgram, 0);
        PDGraph suspect = pdgGenerator.createPDG(suspectProgram, 0);

        String originalGraph = graalAdapter.convertToJson(original);
        String suspectGraph = graalAdapter.convertToJson(suspect);

        model.addAttribute("originalGraph", originalGraph);
        model.addAttribute("suspectGraph", suspectGraph);

        GraalResult graalResult = graalAlgorithm.execute(original, suspect);
        String alignmentJson = graalAdapter.convertToJson(graalResult.findBestAlignment());
        model.addAttribute("alignment", alignmentJson);
        return "index";
    }

}
