USE [master]
GO
/****** Object:  Database [BookShop]    Script Date: 2/25/2025 8:40:44 PM ******/
CREATE DATABASE [BookShop]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'BookShop', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\BookShop.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'BookShop_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\BookShop_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [BookShop] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [BookShop].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [BookShop] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [BookShop] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [BookShop] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [BookShop] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [BookShop] SET ARITHABORT OFF 
GO
ALTER DATABASE [BookShop] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [BookShop] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [BookShop] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [BookShop] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [BookShop] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [BookShop] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [BookShop] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [BookShop] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [BookShop] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [BookShop] SET  ENABLE_BROKER 
GO
ALTER DATABASE [BookShop] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [BookShop] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [BookShop] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [BookShop] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [BookShop] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [BookShop] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [BookShop] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [BookShop] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [BookShop] SET  MULTI_USER 
GO
ALTER DATABASE [BookShop] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [BookShop] SET DB_CHAINING OFF 
GO
ALTER DATABASE [BookShop] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [BookShop] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [BookShop] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [BookShop] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [BookShop] SET QUERY_STORE = ON
GO
ALTER DATABASE [BookShop] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [BookShop]
GO
/****** Object:  Table [dbo].[Authors]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Authors](
	[author_id] [int] IDENTITY(1,1) NOT NULL,
	[author_name] [nvarchar](100) NOT NULL,
	[image] [nvarchar](max) NULL,
	[description] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[author_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BookAuthors]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BookAuthors](
	[book_id] [int] NOT NULL,
	[author_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[book_id] ASC,
	[author_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Books]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Books](
	[book_id] [int] IDENTITY(1,1) NOT NULL,
	[category_id] [int] NOT NULL,
	[title] [nvarchar](255) NOT NULL,
	[rating] [int] NULL,
	[price] [decimal](10, 2) NOT NULL,
	[view_count] [int] NULL,
	[quantity_in_stock] [int] NULL,
	[quantity_sold] [int] NULL,
	[release_date] [date] NULL,
	[description] [nvarchar](max) NULL,
	[image] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[book_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[category_name] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[category_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Favorites]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Favorites](
	[favorite_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[book_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[favorite_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Notifications]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notifications](
	[notification_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[message] [nvarchar](max) NOT NULL,
	[created_at] [datetime] NULL,
	[is_read] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[notification_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[OrderDetails]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[OrderDetails](
	[order_detail_id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [int] NOT NULL,
	[book_id] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [decimal](10, 2) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[order_detail_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Orders]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[order_date] [datetime] NULL,
	[total_amount] [decimal](10, 2) NOT NULL,
	[status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[role_id] [int] IDENTITY(1,1) NOT NULL,
	[role_name] [nvarchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 2/25/2025 8:40:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[username] [nvarchar](50) NOT NULL,
	[password] [nvarchar](100) NOT NULL,
	[full_name] [nvarchar](100) NOT NULL,
	[email] [nvarchar](100) NOT NULL,
	[role_id] [int] NOT NULL,
	[registration_date] [datetime] NULL,
	[status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Authors] ON 

INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (1, N'J.R.R. Tolkien', N'images/author/author1.jpg', N'British author, best known for The Lord of the Rings and The Hobbit.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (2, N'George R.R. Martin', N'images/author/author2.jpg', N'Author of A Song of Ice and Fire, which was adapted into Game of Thrones.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (3, N'J.K. Rowling', N'images/author/author3.jpg', N'British author, best known for the Harry Potter series.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (4, N'Rick Riordan', N'images/author/author4.jpg', N'Author of Percy Jackson & The Olympians.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (5, N'Richard Dawkins', N'images/author/author5.jpg', N'Biologist and author of The Selfish Gene.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (6, N'Stephen Hawking', N'images/author/author6.jpg', N'Renowned theoretical physicist.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (7, N'Bill Bryson', N'images/author/author7.jpg', N'Science writer, author of A Short History of Nearly Everything.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (8, N'Neil deGrasse Tyson', N'images/author/author8.jpg', N'Astrophysicist and science communicator.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (9, N'Yuval Noah Harari', N'images/author/author9.jpg', N'Historian and author of Sapiens: A Brief History of Humankind.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (10, N'Peter Frankopan', N'images/author/author10.jpg', N'Historian, author of The Silk Roads.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (11, N'Jared Diamond', N'images/author/author11.jpg', N'Anthropologist and author of Guns, Germs, and Steel.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (12, N'Anne Frank', N'images/author/author12.jpg', N'Writer of The Diary of a Young Girl.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (13, N'David McCullough', N'images/author/author13.jpg', N'Author of 1776, focused on American history.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (14, N'Robert C. Martin', N'images/author/author14.jpg', N'Author of Clean Code and advocate for software craftsmanship.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (15, N'Andrew Hunt & David Thomas', N'images/author/author15.jpg', N'Authors of The Pragmatic Programmer.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (16, N'Thomas H. Cormen', N'images/author/author16.jpg', N'Co-author of Introduction to Algorithms.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (17, N'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', N'images/author/author17.jpg', N'Authors of Design Patterns.')
INSERT [dbo].[Authors] ([author_id], [author_name], [image], [description]) VALUES (18, N'Steve McConnell', NULL, N'Author of Code Complete, focused on software engineering best practices.')
SET IDENTITY_INSERT [dbo].[Authors] OFF
GO
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (1, 1)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (2, 1)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (3, 2)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (4, 3)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (5, 4)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (6, 5)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (7, 6)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (8, 7)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (9, 8)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (10, 6)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (11, 9)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (12, 10)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (13, 11)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (14, 12)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (15, 13)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (16, 14)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (17, 15)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (18, 16)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (19, 17)
INSERT [dbo].[BookAuthors] ([book_id], [author_id]) VALUES (20, 18)
GO
SET IDENTITY_INSERT [dbo].[Books] ON 

INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (1, 1, N'The Hobbit', 5, CAST(14.99 AS Decimal(10, 2)), 200, 99, 31, CAST(N'1937-09-21' AS Date), N'Fantasy novel by J.R.R. Tolkien.', N'images/books/book1.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (2, 1, N'The Lord of the Rings', 5, CAST(29.99 AS Decimal(10, 2)), 350, 50, 25, CAST(N'1954-07-29' AS Date), N'Epic fantasy trilogy by J.R.R. Tolkien.', N'images/books/book2.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (3, 1, N'Game of Thrones', 4, CAST(19.99 AS Decimal(10, 2)), 180, 79, 41, CAST(N'1996-08-06' AS Date), N'First book in A Song of Ice and Fire series.', N'images/books/book3.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (4, 1, N'Harry Potter and the Chamber of Secrets', 5, CAST(17.99 AS Decimal(10, 2)), 250, 90, 50, CAST(N'1998-07-02' AS Date), N'Second book in the Harry Potter series.', N'images/books/book4.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (5, 1, N'Percy Jackson & The Olympians', 4, CAST(16.50 AS Decimal(10, 2)), 160, 69, 36, CAST(N'2005-06-28' AS Date), N'Greek mythology meets modern adventure.', N'images/books/book5.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (6, 2, N'The Selfish Gene', 5, CAST(21.99 AS Decimal(10, 2)), 120, 40, 15, CAST(N'1976-01-01' AS Date), N'Groundbreaking work on evolution by Richard Dawkins.', N'images/books/book6.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (7, 2, N'Brief Answers to the Big Questions', 4, CAST(22.50 AS Decimal(10, 2)), 90, 30, 10, CAST(N'2018-10-16' AS Date), N'Final thoughts of Stephen Hawking.', N'images/books/book7.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (8, 2, N'A Short History of Nearly Everything', 5, CAST(24.99 AS Decimal(10, 2)), 100, 50, 20, CAST(N'2003-05-06' AS Date), N'Science explained in a simple way.', N'images/books/book8.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (9, 2, N'Astrophysics for People in a Hurry', 4, CAST(15.99 AS Decimal(10, 2)), 130, 60, 25, CAST(N'2017-05-02' AS Date), N'Neil deGrasse Tyson makes astrophysics fun.', N'images/books/book9.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (10, 2, N'The Grand Design', 4, CAST(20.00 AS Decimal(10, 2)), 110, 35, 18, CAST(N'2010-09-07' AS Date), N'Exploration of the universe by Stephen Hawking.', N'images/books/book10.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (11, 3, N'Sapiens: A Brief History of Humankind', 5, CAST(23.99 AS Decimal(10, 2)), 220, 70, 30, CAST(N'2011-01-01' AS Date), N'A look at the history of our species.', N'images/books/book11.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (12, 3, N'The Silk Roads', 5, CAST(19.99 AS Decimal(10, 2)), 140, 55, 22, CAST(N'2015-08-27' AS Date), N'A new history of the world.', N'images/books/book12.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (13, 3, N'Guns, Germs, and Steel', 5, CAST(21.99 AS Decimal(10, 2)), 180, 60, 26, CAST(N'1997-03-01' AS Date), N'Why some civilizations thrived while others didn’t.', N'images/books/book13.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (14, 3, N'The Diary of a Young Girl', 5, CAST(14.99 AS Decimal(10, 2)), 300, 85, 40, CAST(N'1947-06-25' AS Date), N'Anne Frank’s diary during WWII.', N'images/books/book14.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (15, 3, N'1776', 4, CAST(18.99 AS Decimal(10, 2)), 95, 30, 15, CAST(N'2005-05-24' AS Date), N'A historical account of the American Revolution.', N'images/books/book15.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (16, 4, N'Clean Code', 5, CAST(35.99 AS Decimal(10, 2)), 150, 45, 20, CAST(N'2008-08-01' AS Date), N'A guide to writing better code.', N'images/books/book16.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (17, 4, N'The Pragmatic Programmer', 5, CAST(39.99 AS Decimal(10, 2)), 170, 60, 25, CAST(N'1999-10-20' AS Date), N'Tips for becoming a better software developer.', N'images/books/book17.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (18, 4, N'Introduction to Algorithms', 5, CAST(50.99 AS Decimal(10, 2)), 80, 20, 10, CAST(N'2009-07-31' AS Date), N'A comprehensive guide to algorithms.', N'images/books/book18.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (19, 4, N'Design Patterns', 5, CAST(45.99 AS Decimal(10, 2)), 95, 30, 12, CAST(N'1994-10-21' AS Date), N'The classic book on software design.', N'images/books/book19.jpg')
INSERT [dbo].[Books] ([book_id], [category_id], [title], [rating], [price], [view_count], [quantity_in_stock], [quantity_sold], [release_date], [description], [image]) VALUES (20, 4, N'Code Complete', 5, CAST(42.99 AS Decimal(10, 2)), 110, 40, 18, CAST(N'2004-06-09' AS Date), N'A comprehensive guide to software development.', N'images/books/book20.jpg')
SET IDENTITY_INSERT [dbo].[Books] OFF
GO
SET IDENTITY_INSERT [dbo].[Categories] ON 

INSERT [dbo].[Categories] ([category_id], [category_name]) VALUES (1, N'Fiction')
INSERT [dbo].[Categories] ([category_id], [category_name]) VALUES (2, N'Science')
INSERT [dbo].[Categories] ([category_id], [category_name]) VALUES (3, N'History')
INSERT [dbo].[Categories] ([category_id], [category_name]) VALUES (4, N'Technology')
SET IDENTITY_INSERT [dbo].[Categories] OFF
GO
SET IDENTITY_INSERT [dbo].[Favorites] ON 

INSERT [dbo].[Favorites] ([favorite_id], [user_id], [book_id]) VALUES (1, 2, 1)
INSERT [dbo].[Favorites] ([favorite_id], [user_id], [book_id]) VALUES (2, 3, 2)
SET IDENTITY_INSERT [dbo].[Favorites] OFF
GO
SET IDENTITY_INSERT [dbo].[Notifications] ON 

INSERT [dbo].[Notifications] ([notification_id], [user_id], [message], [created_at], [is_read]) VALUES (1, 2, N'Your order has been shipped.', CAST(N'2025-02-09T16:08:55.670' AS DateTime), 0)
INSERT [dbo].[Notifications] ([notification_id], [user_id], [message], [created_at], [is_read]) VALUES (2, 3, N'Your order is pending.', CAST(N'2025-02-09T16:08:55.670' AS DateTime), 0)
SET IDENTITY_INSERT [dbo].[Notifications] OFF
GO
SET IDENTITY_INSERT [dbo].[OrderDetails] ON 

INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (1, 1, 1, 1, CAST(15.99 AS Decimal(10, 2)))
INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (2, 1, 2, 1, CAST(12.50 AS Decimal(10, 2)))
INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (3, 2, 1, 1, CAST(15.99 AS Decimal(10, 2)))
INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (7, 4, 5, 1, CAST(16.50 AS Decimal(10, 2)))
INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (8, 4, 3, 1, CAST(19.99 AS Decimal(10, 2)))
INSERT [dbo].[OrderDetails] ([order_detail_id], [order_id], [book_id], [quantity], [price]) VALUES (9, 4, 1, 1, CAST(14.99 AS Decimal(10, 2)))
SET IDENTITY_INSERT [dbo].[OrderDetails] OFF
GO
SET IDENTITY_INSERT [dbo].[Orders] ON 

INSERT [dbo].[Orders] ([order_id], [user_id], [order_date], [total_amount], [status]) VALUES (1, 2, CAST(N'2025-02-09T16:08:55.670' AS DateTime), CAST(28.49 AS Decimal(10, 2)), N'completed')
INSERT [dbo].[Orders] ([order_id], [user_id], [order_date], [total_amount], [status]) VALUES (2, 3, CAST(N'2025-02-09T16:08:55.670' AS DateTime), CAST(15.99 AS Decimal(10, 2)), N'pending')
INSERT [dbo].[Orders] ([order_id], [user_id], [order_date], [total_amount], [status]) VALUES (4, 1, CAST(N'2025-02-23T22:24:43.250' AS DateTime), CAST(51.48 AS Decimal(10, 2)), N'checkout')
INSERT [dbo].[Orders] ([order_id], [user_id], [order_date], [total_amount], [status]) VALUES (5, 1, CAST(N'2025-02-23T22:25:24.710' AS DateTime), CAST(0.00 AS Decimal(10, 2)), N'pending')
SET IDENTITY_INSERT [dbo].[Orders] OFF
GO
SET IDENTITY_INSERT [dbo].[Roles] ON 

INSERT [dbo].[Roles] ([role_id], [role_name]) VALUES (1, N'Admin')
INSERT [dbo].[Roles] ([role_id], [role_name]) VALUES (2, N'Customer')
SET IDENTITY_INSERT [dbo].[Roles] OFF
GO
SET IDENTITY_INSERT [dbo].[Users] ON 

INSERT [dbo].[Users] ([user_id], [username], [password], [full_name], [email], [role_id], [registration_date], [status]) VALUES (1, N'admin', N'admin123', N'Admin User', N'admin@bookshop.com', 1, CAST(N'2025-02-09T16:08:55.667' AS DateTime), N'active')
INSERT [dbo].[Users] ([user_id], [username], [password], [full_name], [email], [role_id], [registration_date], [status]) VALUES (2, N'customer1', N'password123', N'John Doe', N'johndoe@example.com', 2, CAST(N'2025-02-09T16:08:55.667' AS DateTime), N'active')
INSERT [dbo].[Users] ([user_id], [username], [password], [full_name], [email], [role_id], [registration_date], [status]) VALUES (3, N'customer2', N'password123', N'Jane Smith', N'janesmith@example.com', 2, CAST(N'2025-02-09T16:08:55.667' AS DateTime), N'active')
SET IDENTITY_INSERT [dbo].[Users] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Users__AB6E6164D2014FF1]    Script Date: 2/25/2025 8:40:44 PM ******/
ALTER TABLE [dbo].[Users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Users__F3DBC572FFF7BC7B]    Script Date: 2/25/2025 8:40:44 PM ******/
ALTER TABLE [dbo].[Users] ADD UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BookAuthors]  WITH CHECK ADD FOREIGN KEY([author_id])
REFERENCES [dbo].[Authors] ([author_id])
GO
ALTER TABLE [dbo].[BookAuthors]  WITH CHECK ADD FOREIGN KEY([book_id])
REFERENCES [dbo].[Books] ([book_id])
GO
ALTER TABLE [dbo].[Books]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[Categories] ([category_id])
GO
ALTER TABLE [dbo].[Favorites]  WITH CHECK ADD FOREIGN KEY([book_id])
REFERENCES [dbo].[Books] ([book_id])
GO
ALTER TABLE [dbo].[Favorites]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Notifications]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[OrderDetails]  WITH CHECK ADD FOREIGN KEY([book_id])
REFERENCES [dbo].[Books] ([book_id])
GO
ALTER TABLE [dbo].[OrderDetails]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[Orders] ([order_id])
GO
ALTER TABLE [dbo].[Orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[Roles] ([role_id])
GO
USE [master]
GO
ALTER DATABASE [BookShop] SET  READ_WRITE 
GO
