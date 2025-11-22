package com.example.bank_rest.service;

import com.example.bank_rest.dto.card.CardDTO;
import com.example.bank_rest.dto.card.TransactionsDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.CardStatus;
import com.example.bank_rest.entity.Transaction;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.AppError;
import com.example.bank_rest.exception.NotFoundException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransactionRepository;
import com.example.bank_rest.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;


    public ResponseEntity<?> getCards(int page, int size){
        if(page <= 0 || size <= 0){
            String message = "Плохой запрос c пагинацией!";
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user= userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));


        int left = (page-1)*size;
        int right = page*size-1;
        List<Card> cards = cardRepository.findByUser(user);

        if(cards.size() < right){
            right = cards.size();
            left = right-size-1;
        }

        if(left < 0){
            left = 0;
        }
        if(right == -1){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()).subList(left, right));
    }

    private CardDTO convertToResponse(Card card){
        CardDTO cardDTO = new CardDTO();

        cardDTO.setId(card.getId());
        cardDTO.setCardNumber(card.getCardNumber());
        cardDTO.setExpiryDate(card.getExpiryDate());
        cardDTO.setStatus(card.getStatus());
        cardDTO.setBalance(card.getBalance());

        return cardDTO;
    }

    public ResponseEntity<?> responseBlockCard(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден"));

        if(!card.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return ResponseEntity.ok(convertToResponse(card));
    }

    public ResponseEntity<?> getCardById(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));


        if(!card.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }


        return ResponseEntity.ok(convertToResponse(card));
    }


    @Transactional
    public ResponseEntity<?> transactions(TransactionsDTO transactionsDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Card fromCard =  cardRepository.findById(transactionsDTO.getFromId())
                .orElseThrow(() -> new NotFoundException("Карта с таким id не найден!"));


        Card toCard;

        if (transactionsDTO.getToId() == -1) {
            toCard = cardRepository.findByCardNumber(transactionsDTO.getToCardNumber())
                    .orElseThrow(() -> new NotFoundException("Карта с таким номером не найден!"));
        } else {
            toCard = cardRepository.findById(transactionsDTO.getToId())
                    .orElseThrow(() -> new NotFoundException("Карта на id которую хотите перевести, не найден!"));
        }



        if(!fromCard.getUser().getUsername().equals(authentication.getName())){
            String message = "Недостаточно прав для доступа к ресурсу";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(), message), HttpStatus.FORBIDDEN);
        }

        if(fromCard.getStatus() != CardStatus.ACTIVE) {
            if (fromCard.getStatus() == CardStatus.BLOCKED) {
                String message = "Ваша карта заблокирована. Обратитесь в банк!";
                return new ResponseEntity<>(new AppError(HttpStatus.LOCKED.value(), message), HttpStatus.LOCKED);
            } else {
                String message = "Ваша карта истекла. Обратитесь в банк!";
                return new ResponseEntity<>(new AppError(HttpStatus.GONE.value(), message), HttpStatus.GONE);
            }
        }

        if(toCard.getStatus() != CardStatus.ACTIVE) {
            if (toCard.getStatus() == CardStatus.BLOCKED) {
                String message = "Карта к которую хотите отправить деньги заблокирована!";
                return new ResponseEntity<>(new AppError(HttpStatus.LOCKED.value(), message), HttpStatus.LOCKED);
            } else {
                String message = "Карта к которую хотите отправить деньги срок действия истек!";
                return new ResponseEntity<>(new AppError(HttpStatus.GONE.value(), message), HttpStatus.GONE);
            }
        }

        if(fromCard.getBalance() - transactionsDTO.getAmount() < 0){
            String message = "Недостоточно средств";
            return new ResponseEntity<>(new AppError(HttpStatus.PAYMENT_REQUIRED.value(), message), HttpStatus.PAYMENT_REQUIRED);
        }

        fromCard.setBalance(fromCard.getBalance() - transactionsDTO.getAmount());
        toCard.setBalance(toCard.getBalance() + transactionsDTO.getAmount());


        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        Transaction transaction = new Transaction(transactionsDTO.getAmount(), transactionsDTO.getFromId(), toCard.getId(), transactionsDTO.getDescription());



        return ResponseEntity.ok(transactionRepository.save(transaction));
    }
}
