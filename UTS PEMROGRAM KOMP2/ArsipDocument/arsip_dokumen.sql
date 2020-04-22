-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 22 Apr 2020 pada 17.17
-- Versi server: 10.1.32-MariaDB
-- Versi PHP: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `arsip_dokumen`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `passw` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`admin_id`, `username`, `passw`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3');

-- --------------------------------------------------------

--
-- Struktur dari tabel `data_arsip`
--

CREATE TABLE `data_arsip` (
  `id` int(11) NOT NULL,
  `kode_dok` int(11) NOT NULL,
  `nama_dok` varchar(100) NOT NULL,
  `kategori_dok` varchar(100) NOT NULL,
  `lokasi_dok` blob NOT NULL,
  `deskripsi_dok` varchar(100) NOT NULL,
  `tanggal` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `data_arsip`
--

INSERT INTO `data_arsip` (`id`, `kode_dok`, `nama_dok`, `kategori_dok`, `lokasi_dok`, `deskripsi_dok`, `tanggal`) VALUES
(17, 1, 'Bisnis', '1', 0x433a55736572735075626c69634465736b746f7046697265666f782e6c6e6b, 'Bisni Sicantik Store', '20 APRIL 2020'),
(18, 2, 'Tugas Pemrogram', '3', 0x443a42657266756e67736920756e74756b2e646f6378, 'Tugas Pemrogram tentang MYSQL', '20 APRIL 2020');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `data_arsip`
--
ALTER TABLE `data_arsip`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode_document` (`kode_dok`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `data_arsip`
--
ALTER TABLE `data_arsip`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
