package cc.carce.sale.service;

import cc.carce.sale.entity.ReservationEntity;
import cc.carce.sale.mapper.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationEntity saveReservation(ReservationEntity reservation) {
        // set createTime if not provided
        if (reservation.getCreateTime() == null) {
            reservation.setCreateTime(new java.util.Date());
        }
        return reservationRepository.save(reservation);
    }
}
