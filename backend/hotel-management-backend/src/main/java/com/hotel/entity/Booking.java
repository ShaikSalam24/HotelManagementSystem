package com.hotel.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Booking  extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String bookingNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private User customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;
	
	private LocalDate checkInDate;

	private LocalDate checkOutDate;

	private Integer numberOfGuests;

	private BigDecimal totalAmount;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;
	
	@Column(length = 500)
	private String specialRequest;
}
