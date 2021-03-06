
package monprojet.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import monprojet.dao.CityRepository;
import monprojet.dao.CountryRepository;
import monprojet.entity.City;
import monprojet.entity.Country;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping(path = "/cities") 
@Slf4j
public class CreerPaysController{
	
	private static final String DEFAULT_VIEW = "cities";
        @Autowired
	private CityRepository cityDao;
	@Autowired
	private CountryRepository countryDao;	
	/**
	 * Affiche la page d'édition des villes
	 * @param model Les infos transmises à la vue (injecté par Spring)
	 * @return le nom de la vue à afficher
	 */	
	@GetMapping(path = "show") //à l'URL http://localhost:8989/cities/show
	public String montreLesVilles(Model model) {
		log.info("On affiche les villes");
		// On initialise la ville avec des valeurs par défaut
		Country france = countryDao.findById(1).orElseThrow();
		City nouvelle = new City("Nouvelle ville", france);
		nouvelle.setPopulation(50);
		model.addAttribute("cities", cityDao.findAll());
		model.addAttribute("city", nouvelle);
		model.addAttribute("countries", countryDao.findAll());
		return DEFAULT_VIEW;
	}
        /**
	 * Insère une nouvelle ville dans la base
	 * @param laVille la ville à insérer, initialisée par Spring à partir des valeurs soumises dans le formulaire
	 * Spring fera automatiquement une requête SQL SELECT pour récupérer le pays à partir de son id.	 
	 * Spring fera une requête SQL INSERT pour insérer la ville dans la base
	 * @return
	 */
	@PostMapping(path="save") // Requête HTTP POST à l'URL http://localhost:8989/cities/save
	public String enregistreUneVille(City laVille) {
		cityDao.save(laVille);
		log.info("La ville {} a été enregistrée", laVille);
		// On redirige vers la page de liste des villes
		return "redirect:/cities/show";
	}
         
	@GetMapping(path = "edit")
	public String montreLeFormulairePourEdition(@RequestParam("id") int id, Model model) {
		model.addAttribute("city",cityDao.findById(id).get());
                model.addAttribute("countries",countryDao.findAll());
                return "edit";
	}
        
        @GetMapping(path = "delete")
	public String supprimeUneVille(@RequestParam("id") City laVille) {
		cityDao.delete(laVille); 
		return "redirect:/cities/show"; 
	}
}
  



