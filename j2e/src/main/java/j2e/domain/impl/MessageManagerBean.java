package j2e.domain.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import j2e.domain.CanalFinder;
import j2e.domain.MessageFinder;
import j2e.domain.MessageManager;
import j2e.domain.UtilisateurFinder;
import j2e.entities.Canal;
import j2e.entities.Message;
import j2e.entities.Utilisateur;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@WebService
public class MessageManagerBean implements MessageManager{
	
	 @PersistenceContext(unitName = "polytweet-pu")
	    EntityManager entityManager;

	    @EJB
	    UtilisateurFinder utilisateurFinder;
	    
	    @EJB
	    CanalFinder canalFinder;
	    
	    @EJB
	    MessageFinder messageFinder;

	    
	    public boolean delete(long id) {
	        Message message = messageFinder.findMessageById(id);
	        if (message != null){
	            entityManager.remove(message);
	            return true;
	        }
	        return false;
	    }

	    public long create(String texte, String tagCanal, String loginAuteur){
            Canal canal = canalFinder.findCanalByTag(tagCanal);
	        Utilisateur auteur = utilisateurFinder.findUtilisateurByLogin(loginAuteur);
	        entityManager.merge(canal);
	        entityManager.merge(auteur);
	        Message message = new Message(texte, canal, auteur);
	        canal.ajouterMessage(message);
	        entityManager.persist(message);
	        entityManager.merge(canal);
	        entityManager.merge(auteur);
	        return message.getId();
	    }

	    @PostConstruct
	    public void initialize() {
	        System.out.println("Initializing MessageManager");
	    }

	    @PreDestroy
	    public void cleanup() {
	        System.out.println("Destroying MessageManager");
	    }

		public Set<Long> afficherIdByTag(String tagCanal) {
			Set<Long> set = new HashSet<Long>();
			for (Message m : messageFinder.findMessageByTag(tagCanal)) set.add(m.getId());
			return set;
		}

		public String afficherTexteByID(long id) {
			return messageFinder.findMessageById(id).getTexte();
		}

		public String afficherAuteurByID(long id) {
			return messageFinder.findMessageById(id).getAuteur().getLogin();
		}

		public String afficherDateByID(long id) {
			try {
				Message m = messageFinder.findMessageById(id);
				if (m==null) return "m null";
				Date d = m.getDate();
				if (d==null) return "d null";
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
			return df.format(messageFinder.findMessageById(id).getDate());
			} catch (Exception e) { 
				return e.toString();
			}
		}

}
