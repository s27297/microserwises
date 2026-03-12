package com.pjatk.users.adapters.persistance.repositories;

import com.pjatk.users.adapters.persistance.model.UserDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    record LabeledTotal(String label, double total) {}
    record MonthlyTotal(String ym, double total) {}

    @Aggregation(pipeline = {
            "{ $unwind: '$payments' }",
            "{ $group: { _id: '$login', total: { $sum: '$payments.quantity' } } }",
            "{ $project: { _id: 0, label: '$_id', total: 1 } }",
            "{ $sort: { total: -1 } }"
    })
    List<LabeledTotal> sumPaymentsPerUser();

    @Aggregation(pipeline = {
            "{ $unwind: '$payments' }",
            "{ $addFields: { ym: { $dateToString: { format: '%Y-%m', date: { $toDate: '$payments.date' } } } } }",
            "{ $group: { _id: '$ym', total: { $sum: '$payments.quantity' } } }",
            "{ $project: { _id: 0, ym: '$_id', total: 1 } }",
            "{ $sort: { ym: 1 } }"
    })
    List<MonthlyTotal> sumByMonth();

    @Aggregation(pipeline = {
            "{ $unwind: '$payments' }",
            "{ $group: { _id: '$type', total: { $sum: '$payments.quantity' } } }",
            "{ $project: { _id: 0, label: '$_id', total: 1 } }",
            "{ $sort: { total: -1 } }"
    })
    List<LabeledTotal> sumPerType();

    @Query(value = "{ 'login': { $regex: ?0, $options: 'i' } }")
    List<UserDocument> findByLoginPrefixRegex(String anchoredPrefixRegex, Pageable pageable);
    Optional<UserDocument> findByLogin(String login);
}
