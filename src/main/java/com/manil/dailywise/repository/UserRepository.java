package com.manil.dailywise.repository;

import com.manil.dailywise.entity.User;
import com.manil.dailywise.enums.user.SnsType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Long deleteByEmail(String email);
    Optional<User> findByUniqId(String uniqId);
    List<User> findAllByUpdateWiseTime(String time);
    Optional<User> findByProviderIdAndProvider(String providerId, SnsType provider);

    @Query(nativeQuery = true,
            value="SELECT u.* " +
                    "FROM `users` u " +
//                    "LEFT JOIN `wise` w ON u.uniq_id = w.writer " +
                    "LEFT JOIN follow f ON u.id = f.follow_to_id " +
                    "WHERE u.verified = true " +
                    "AND u.is_active = true " +
//                    "AND (u.uniq_id LIKE %:search% OR u.nickname LIKE %:search% OR w.title like %:search%) " +
                    "AND (u.uniq_id LIKE %:search% OR u.nickname LIKE %:search%) " +
//                    "GROUP BY u.id " +
                    "ORDER BY u.is_writer DESC, u.is_master_piece DESC, count(f.id) DESC "
    )
    List<User> getUsers(String search);

    @Query(nativeQuery = true,
            value="SELECT u.* " +
                    "FROM `users` u " +
                    "WHERE u.verified = true " +
                    "AND u.is_active = true " +
                    "AND u.uniq_id like %:filter% OR u.nickname like %:filter% " +
                    "ORDER BY u.is_writer DESC, u.is_master_piece"
    )
    List<User> getUsersWithFilters(String filter);
}
