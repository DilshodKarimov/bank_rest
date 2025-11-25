package com.example.bank_rest.service.admin;

import com.example.bank_rest.dto.card.CreateCardDTO;
import com.example.bank_rest.dto.card.DeleteResponseDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public List<Card> getCards(int page, int size){

        int left = (page-1)*size;
        int right = page*size-1;

        List<Card> cardList = cardRepository.findAll();

        if(cardList.size() < right){
            right = cardList.size();
            left = right-size-1;
        }
        if(left < 0){
            left = 0;
        }
        if(right == -1){
            return null;
        }
        return cardList.subList(left , right);
    }

    public Card getCardById(Long id){
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        return card;
    }

    public Card changeStatus(Card card){
        return cardRepository.save(card);
    }

    public DeleteResponseDTO deleteCard(Long id){
        Card card = cardRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        cardRepository.deleteById(id);

        return new DeleteResponseDTO();
    }

    public String getUserByCardId(Long id){
        Card card = cardRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        String redirect = "redirect:/admin/user/" + card.getUser().getId().toString();
        return redirect;
    }

    public List<Card> getCardsByUserId(Long id, int page, int size){

        int left = (page-1)*size;
        int right = page*size-1;

        User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет!"));

        List<Card> cardList = cardRepository.findByUser(user);

        if(cardList.size() < right){
            right = cardList.size();
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }

        if(right == -1){
            return null;
        }

        return cardList.subList(left , right);
    }

    public Card createCard(CreateCardDTO createCardDTO){
        Card card = new Card();
        card.setBalance(createCardDTO.getBalance());
        card.setExpiryDate(createCardDTO.getExpired());
        card.setStatus(CardStatus.ACTIVE);
        User user = userRepository.findByUsername(createCardDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден!"));

        card.setUser(user);
        cardRepository.save(card);
        card.setCardNumber(createCardNumber(card.getId()));
        return cardRepository.save(card);
    }

    private String createCardNumber(Long id){
        String a = id.toString();
        String zero = "0".repeat(12-a.length());
        return zero+a;
    }


}
