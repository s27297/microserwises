package com.pjatk.users.adapters.persistance;

import com.pjatk.users.adapters.persistance.mappers.UserMapper;
import com.pjatk.users.adapters.persistance.model.UserDocument;
import com.pjatk.users.adapters.persistance.repositories.UserRepository;
import com.pjatk.users.core.port.out.UserWritePort;
import com.pjatk.users.core.port.out.UsersReadPort;
import com.pjatk.users.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UsersMongoAdapter implements UsersReadPort, UserWritePort {
    private final UserRepository repo;
    private final MongoTemplate mongo;
    @Override
    public List<User> getUsers(Pageable pageable) {
        List<UserDocument> users=repo.findAll(pageable).stream().toList();
        return users.stream()
                .map(UserMapper::toUser).toList();
    }

    @Override
    public List<User> getUsersLogins(Pageable pageable) {
        List<UserDocument> users=repo.findAll(pageable).stream().toList();
        return users.stream()
                .map(user->
                        new User(user.getLogin())).toList();
    }

    @Override
    public List<User> findByLoginPrefix(String prefix, Pageable pageable) {
        String pattern = (prefix == null || prefix.isBlank())
                ? ".*"
                : "^" + Pattern.quote(prefix);
        List<UserDocument> u=repo.findByLoginPrefixRegex(pattern,pageable);
        return repo.findByLoginPrefixRegex(pattern, pageable).stream()
                .map(d -> new User(d.getId(),d.getName(),d.getSurname(),d.getLogin(),
                        d.getDateOfBirth(),d.getDateOfJoining(),
                        d.getEmail(), d.getNumerTelefonu(), d.getType(),
                        d.getSprawnosci().stream().map(l -> new User.Sprawnosc(l.source_id(),l.sprawnosc_type(), l.sprawnosc_name()) ).toList(),
                        d.getPayments().stream().map(l -> new User.Payment(l.sorce_id(),l.nazwa(),l.date(), l.quantity()) ).toList()
                ))
                .toList();
    }

    @Override
    public Optional<User> getUserById(String id) {
        Optional<UserDocument> user=repo.findById(id);
        if(user.isPresent()){
            return Optional.of(UserMapper.toUser(repo.findById(id).get()));
        }
        else return Optional.empty();
    }

    @Override
    public String save(User user) {
      return repo.save(UserMapper.toUserDocument(user)).getId();
    }

    @Override
    public User update(String id, User user) {
        if(repo.findById(id).isPresent()) {
            UserDocument userDocument = repo.findById(id).get();
            userDocument.setName(user.getName());
            userDocument.setSurname(user.getSurname());
            userDocument.setLogin(user.getLogin());
            userDocument.setEmail(user.getEmail());
            userDocument.setNumerTelefonu(user.getNumerTelefonu());
            userDocument.setDateOfBirth(user.getDateOfBirth());
            repo.save(userDocument);
            return UserMapper.toUser(userDocument);
        }
        else return null;
    }

    @Override
    public boolean deleteById(String id) {
        Optional<UserDocument> userDocument = repo.findById(id);
        repo.deleteById(id);
        if(userDocument.isPresent())return true;
        return false;
    }

    @Override
    public String addSprawnosc(String id,User.Sprawnosc sprawnosc) {
        Optional<UserDocument> userDocument = repo.findById(id);
        if (userDocument.isPresent()){
            Optional<UserDocument.Sprawnosc> sprawnosc1= userDocument.get().getSprawnosci().stream()
                    .filter(p-> Objects.equals(p.source_id(), sprawnosc.source_id())).toList().stream().findFirst();
            if(sprawnosc1.isPresent()) {
                return "source_id must be unique";
            }


            userDocument.get().getSprawnosci().add(UserMapper.toSprawnoscDocument(sprawnosc));
            repo.save(userDocument.get());
            return "sprawnosc added";
        }
        else return "user not Found";
    }

    @Override
    public String deleteSprawnosc(String id, Integer source_id) {
        Optional<UserDocument> userDocument = repo.findById(id);
        if (userDocument.isPresent()){

           Optional<UserDocument.Sprawnosc> sprawnosc= userDocument.get().getSprawnosci().stream()
                    .filter(p->p.source_id()==source_id).toList().stream().findFirst();
           if(sprawnosc.isPresent()){
               userDocument.get().getSprawnosci().remove(sprawnosc.get());
               repo.save(userDocument.get());
               return "sprawnosc deleted";

           }
           else return "sprawnosc not found";
        }
        else return "user not Found";
    }

    @Override
    public String addPayment(String id,User.Payment payment) {
        Optional<UserDocument> userDocument = repo.findById(id);
        if (userDocument.isPresent()){
            Optional<UserDocument.Payment> payment1= userDocument.get().getPayments().stream()
                    .filter(p-> Objects.equals(p.sorce_id(), payment.source_id())).toList().stream().findFirst();
            if(payment1.isPresent()) {
                return "source_id must be unique";
            }
            userDocument.get().getPayments().add(UserMapper.toPaymentDocument(payment));
            repo.save(userDocument.get());
            return "payment added";
        }
        else return "user not Found";
    }

    @Override
    public String deletePayment(String id, Integer source_id) {
        Optional<UserDocument> userDocument = repo.findById(id);
        if (userDocument.isPresent()){

            Optional<UserDocument.Payment> payment= userDocument.get().getPayments().stream()
                    .filter(p-> Objects.equals(p.sorce_id(), source_id)).toList().stream().findFirst();
            if(payment.isPresent()){
                userDocument.get().getPayments().remove(payment.get());
                repo.save(userDocument.get());
                return "payment deleted";

            }
            else return "payment not found";
        }
        else return "user not Found";
    }

    @Override
    public String updateSprawnosc(String id, User.Sprawnosc sprawnosc) {
        Optional<UserDocument> optionalUser = repo.findById(id);
        if (optionalUser.isEmpty()) {
            return "user not Found";
        }
        UserDocument user = optionalUser.get();
        Optional<UserDocument.Sprawnosc> existing =
                user.getSprawnosci().stream()
                        .filter(s -> Objects.equals(s.source_id(), sprawnosc.source_id()))
                        .findFirst();
        if (existing.isEmpty()) {
            return "sprawnosc not found";
        }
        UserDocument.Sprawnosc updated =
                new UserDocument.Sprawnosc(
                        sprawnosc.source_id(),
                        sprawnosc.sprawnosc_type(),
                        sprawnosc.sprawnosc_name()
                );

        user.setSprawnosci(user.getSprawnosci().stream().map(
                a-> Objects.equals(a.source_id(), sprawnosc.source_id()) ?updated:a
        ).collect(Collectors.toCollection(ArrayList::new)));

        repo.save(user);
        return "sprawnosc updated";
    }

    @Override
    public String updatePayment(String id, User.Payment payment) {
        Optional<UserDocument> optionalUser = repo.findById(id);
        if (optionalUser.isEmpty()) {
            return "user not Found";
        }
        UserDocument user = optionalUser.get();
        Optional<UserDocument.Payment> existing =
                user.getPayments().stream()
                        .filter(p -> Objects.equals(p.sorce_id(), payment.source_id()))
                        .findFirst();

        if (existing.isEmpty()) {
            return "payment not found";
        }

        UserDocument.Payment updated =
                new UserDocument.Payment(
                        payment.source_id(),
                        payment.nazwa(),
                        payment.date(),
                        payment.quantity()
                );
        user.setPayments(user.getPayments().stream().map(
                a-> Objects.equals(a.sorce_id(), payment.source_id()) ?updated:a
        ).collect(Collectors.toCollection(ArrayList::new)));
        repo.save(user);
        return "payment updated";
    }

}
