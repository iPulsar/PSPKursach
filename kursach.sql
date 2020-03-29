-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Хост: localhost
-- Время создания: Мар 29 2020 г., 15:12
-- Версия сервера: 10.3.18-MariaDB-0+deb10u1
-- Версия PHP: 7.3.11-1~deb10u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `kursach`
--

-- --------------------------------------------------------

--
-- Структура таблицы `advertise`
--

CREATE TABLE `advertise` (
  `advertiseid` int(11) NOT NULL,
  `ownerid` int(11) NOT NULL,
  `advertise` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `expiredate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `advertise`
--

INSERT INTO `advertise` (`advertiseid`, `ownerid`, `advertise`, `date`, `expiredate`) VALUES
(1, 3, 'Here we post some ads. Contact to app owner to place ad here', '2020-03-27 12:03:38', '2020-05-01'),
(3, 1, 'Senya mudila', '2020-03-29 16:10:37', '2020-04-01');

-- --------------------------------------------------------

--
-- Структура таблицы `board`
--

CREATE TABLE `board` (
  `userid` int(11) NOT NULL,
  `bmessage` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `board`
--

INSERT INTO `board` (`userid`, `bmessage`, `date`) VALUES
(3, 'hi evryone', '2020-03-27 06:34:38'),
(3, 'Hi everyone ', '2020-03-27 08:03:02'),
(3, 'Hi guys', '2020-03-27 08:09:00'),
(3, '1', '2020-03-29 14:09:59');

-- --------------------------------------------------------

--
-- Структура таблицы `groups`
--

CREATE TABLE `groups` (
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `gname` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура таблицы `messages`
--

CREATE TABLE `messages` (
  `senderid` int(11) NOT NULL,
  `reciverid` int(11) NOT NULL,
  `message` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `anon` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `messages`
--

INSERT INTO `messages` (`senderid`, `reciverid`, `message`, `date`, `anon`) VALUES
(3, 6, 'Hello world', '2020-03-26 17:11:40', 0),
(3, 6, 'ti pidor', '2020-03-26 18:22:48', 1),
(3, 6, 'privet', '2020-03-26 18:32:16', 0),
(3, 6, 'privet', '2020-03-27 06:18:17', 0),
(3, 3, 'test', '2020-03-29 18:10:13', 1),
(3, 3, 'neopulsar privet', '2020-03-29 18:22:56', 0),
(1, 3, 'privet', '2020-03-29 18:23:45', 0),
(3, 1, 'privet', '2020-03-29 18:23:59', 0),
(3, 1, 'anon kak dela', '2020-03-29 18:24:41', 0),
(1, 1, 'kak dela anon?', '2020-03-29 18:26:10', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int(255) NOT NULL,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `token` text NOT NULL,
  `lastlogin` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `tokenexpire` timestamp NOT NULL DEFAULT current_timestamp(),
  `FirstName` text NOT NULL,
  `LastName` text NOT NULL,
  `country` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `token`, `lastlogin`, `tokenexpire`, `FirstName`, `LastName`, `country`) VALUES
(1, 'anon', 'anon', '21c3f1919faf718e', '2020-03-30 02:23:33', '2020-03-30 06:23:33', 'anon', 'anon', 'anon'),
(3, 'neopulsar', 'Alfa1Beta', '99829e22d2d7b6', '2020-03-30 04:08:46', '2020-03-30 08:08:46', 'Vladislav', 'Orlovskiy', 'Belarus'),
(5, 'test2', 'test2', '2c61f10e0b7fef6f', '2020-03-26 10:04:52', '2020-03-26 10:04:52', 'test2', 'test2', 'test2'),
(6, 'senya', 'tipidor', '2748e6a26f5e3432', '2020-03-26 22:57:19', '2020-03-27 02:57:19', 'arseniy', 'nimera', 'belarus');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `advertise`
--
ALTER TABLE `advertise`
  ADD PRIMARY KEY (`advertiseid`),
  ADD UNIQUE KEY `advertiseid` (`advertiseid`),
  ADD KEY `advertiseid_2` (`advertiseid`),
  ADD KEY `ownerid` (`ownerid`) USING BTREE;

--
-- Индексы таблицы `board`
--
ALTER TABLE `board`
  ADD KEY `userid` (`userid`) USING BTREE;

--
-- Индексы таблицы `groups`
--
ALTER TABLE `groups`
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `id_2` (`id`);

--
-- Индексы таблицы `messages`
--
ALTER TABLE `messages`
  ADD KEY `senderid` (`senderid`) USING BTREE,
  ADD KEY `reciverid` (`reciverid`) USING BTREE;

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_3` (`id`),
  ADD KEY `id` (`id`),
  ADD KEY `id_2` (`id`),
  ADD KEY `id_4` (`id`),
  ADD KEY `id_5` (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `advertise`
--
ALTER TABLE `advertise`
  MODIFY `advertiseid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `advertise`
--
ALTER TABLE `advertise`
  ADD CONSTRAINT `advertise_ibfk_1` FOREIGN KEY (`ownerid`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Ограничения внешнего ключа таблицы `board`
--
ALTER TABLE `board`
  ADD CONSTRAINT `board_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Ограничения внешнего ключа таблицы `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`reciverid`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`senderid`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
