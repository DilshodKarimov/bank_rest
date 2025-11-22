package com.example.bank_rest.service.admin;

import com.example.bank_rest.dto.card.CardStatusDTO;
import com.example.bank_rest.dto.card.CreateCardDTO;
import com.example.bank_rest.dto.card.DeleteResponseDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getCards(int page, int size){
        int left = (page-1)*size;
        int right = page*size-1;
        List<Card> cardList = cardRepository.findAll();
        if(page <= 0 || size <= 0){
            String message = "Плохой запрос!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }
        if(cardList.size() < right){
            right = cardList.size()-1;
            left = right-size-1;
        }
        if(left < 0){
            left = 0;
        }
        return ResponseEntity.ok(cardList.subList(left , right));
    }

    public ResponseEntity<?> getCardById(Long id){
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        return ResponseEntity.ok(card);
    }

    public ResponseEntity<?> changeStatus(Long id, CardStatusDTO cardStatusDTO){
        Card card = cardRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        if(card.getStatus() == CardStatus.EXPIRED && cardStatusDTO.getStatus() == CardStatus.ACTIVE){
            if(cardStatusDTO.getExpiryDate() == null){
                String message = "Для того чтобы продлить карту нужно указать новый срок карты!";
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
            }
            card.setExpiryDate(cardStatusDTO.getExpiryDate());
        }

        card.setStatus(cardStatusDTO.getStatus());

        return ResponseEntity.ok(cardRepository.save(card));
    }

    public ResponseEntity<?> deleteCard(Long id){
        Card card = cardRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        cardRepository.deleteById(id);

        return ResponseEntity.ok(new DeleteResponseDTO());
    }

    public ResponseEntity<?> getUserByCardId(Long id){
        Card card = cardRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Карта с таким id не существует!"));

        String redirect = "redirect:/admin/user/" + card.getUser().getId().toString();
        return ResponseEntity.ok(redirect);
    }

    public ResponseEntity<?> getCardByUserId(Long id, int page, int size){

        int left = (page-1)*size;
        int right = page*size-1;


        User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет!"));


        List<Card> cardList = cardRepository.findByUser(user);

        if(page <= 0 || size <= 0){
            String message = "Плохой запрос c пагинацией!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }


        if(cardList.size() < right){
            right = cardList.size()-1;
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }

        return ResponseEntity.ok(cardList.subList(left , right));
    }


    public ResponseEntity<?> createCard(CreateCardDTO createCardDTO){
        Card card = new Card();
        card.setBalance(createCardDTO.getBalance());
        card.setExpiryDate(createCardDTO.getExpired());
        card.setStatus(CardStatus.ACTIVE);
        User user = userRepository.findByUsername(createCardDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден!"));

        card.setUser(user);
        cardRepository.save(card);
        card.setCardNumber(createCardNumber(card.getId()));
        return ResponseEntity.ok(cardRepository.save(card));
    }

    private String createCardNumber(Long id){
        String a = id.toString();
        String zero = "0".repeat(12-a.length());
        return zero+a;
    }


}
