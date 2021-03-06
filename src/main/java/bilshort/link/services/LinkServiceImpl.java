package bilshort.link.services;

import bilshort.link.models.Link;
import bilshort.link.models.LinkDTO;
import bilshort.link.repositories.LinkRepository;
import bilshort.user.models.User;
import bilshort.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Link createShortLink(LinkDTO linkDTO) {
        Link link = new Link();

        link.setUrl(linkDTO.getUrl());
        link.setDescription(linkDTO.getDescription());
        link.setExpTime(linkDTO.getExpTime());
        link.setCode(linkDTO.getCode());

        Date date = new Date();

        if (linkDTO.getExpTime() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 7);

            link.setExpTime(calendar.getTime().getTime() / 1000);
        }
        else {
            String expTimeHandler = "" + linkDTO.getExpTime();
            if (expTimeHandler.length() < 10){
                return null;
            }
            Long expTime = Long.parseLong(expTimeHandler.substring(0, 10));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (expTime < calendar.getTime().getTime() / 1000){
                return null;
            }
            link.setExpTime(expTime);
        }

        link.setCreatedAt(date.getTime() / 1000);

        if (linkDTO.getUserName().equals("anonymousUser")) {
            User user = new User();
            // 0 -> Anonymous User.
            user.setUserId(0);
            link.setOwner(user);
        }
        else {
            link.setOwner(userRepository.findByUserName(linkDTO.getUserName()));
        }

        return linkRepository.save(link);
    }

    @Override
    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    @Override
    public Link getLinkById(Integer id) {
        return linkRepository.findByLinkId(id);
    }

    @Override
    public Long deleteLinkById(Integer id) {
        return linkRepository.deleteByLinkId(id);
    }

    @Override
    public Link updateLink(Link link) {
        Date date = new Date();
        String expTimeHandler = "" + link.getExpTime();
        if (expTimeHandler.length() < 10){
            return null;
        }
        Long expTime = Long.parseLong(expTimeHandler.substring(0, 10));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (expTime < calendar.getTime().getTime() / 1000){
            return null;
        }
        link.setExpTime(expTime);
        return linkRepository.save(link);
    }

    @Override
    public List<Link> getLinksByUserId(Integer userId) {
        return linkRepository.findByUserIdEx(userId);
    }

    @Override
    public Link getLinkByCode(String code) {
        return linkRepository.findByCode(code);
    }

    @Override
    public List<Link> getLinksByUserName(String userName) {
        return linkRepository.findByUserNameEx(userName);
    }
}