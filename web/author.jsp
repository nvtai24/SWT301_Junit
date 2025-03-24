

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!doctype html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Book Library</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="apple-touch-icon" href="apple-touch-icon.png">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/font-awesome.min.css">
        <link rel="stylesheet" href="css/icomoon.css">
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/owl.carousel.css">
        <link rel="stylesheet" href="css/transitions.css">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/color.css">
        <link rel="stylesheet" href="css/responsive.css">
        <script src="js/vendor/modernizr-2.8.3-respond-1.4.2.min.js"></script>
        <style>
            .author-form-container {
    max-width: 800px;
    margin: 30px auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.author-form-container h2 {
    text-align: center;
    margin-bottom: 20px;
    font-size: 1.8em;
    color: #333;
}

.author-form-container .form-group {
    margin-bottom: 20px;
}

.author-form-container .form-group label {
    font-size: 1.1em;
    color: #555;
    margin-bottom: 8px;
    display: block;
}

.author-form-container .form-group input, 
.author-form-container .form-group textarea, 
.author-form-container .form-group select {
    width: 100%;
    padding: 12px;
    font-size: 1em;
    border: 1px solid #ddd;
    border-radius: 5px;
    box-sizing: border-box;
    margin-top: 5px;
    transition: border-color 0.3s ease;
}

.author-form-container .form-group input:focus, 
.author-form-container .form-group textarea:focus, 
.author-form-container .form-group select:focus {
    border-color: #3498db;
    outline: none;
}

.author-form-container .form-group textarea {
    min-height: 150px;
}

.author-form-container .btn {
    display: inline-block;
    padding: 12px 20px;
    font-size: 1.1em;
    color: #fff;
    background-color: #3498db;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    text-align: center;
    width: 100%;
    margin-top: 20px;
}

.author-form-container .btn:hover {
    background-color: #2980b9;
}

.author-form-container .form-group input[type="file"] {
    padding: 5px;
    font-size: 1em;
    border: 1px solid #ddd;
}

.form-footer {
    margin-top: 30px;
    text-align: center;
}

.form-footer a {
    color: #3498db;
    text-decoration: none;
}

.form-footer a:hover {
    text-decoration: underline;
}

/* Responsive adjustments */
@media (max-width: 768px) {
    .author-form-container {
        padding: 15px;
    }

    .author-form-container h2 {
        font-size: 1.5em;
    }

    .author-form-container .form-group {
        margin-bottom: 15px;
    }

    .author-form-container .btn {
        font-size: 1em;
    }
}
        </style>
            
    </head>
    <body>

        <div id="tg-wrapper" class="tg-wrapper tg-haslayout">
            <!--************************************
                            Header Start
            *************************************-->
            <jsp:include page="header.jsp" />

            <!--************************************
                            Header End
            *************************************-->
            <!--************************************
                            Inner Banner Start
            *************************************-->
            <div class="tg-innerbanner tg-haslayout tg-parallax tg-bginnerbanner" data-z-index="-100" data-appear-top-offset="600" data-parallax="scroll" data-image-src="images/parallax/bgparallax-07.jpg">
                <div class="container">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="tg-innerbannercontent">
                                <h1>Authors</h1>
                                <ol class="tg-breadcrumb">
                                    <li><a href="home">home</a></li>
                                    <li class="tg-active">Authors</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--************************************
                            Inner Banner End
            *************************************-->
            <!--************************************
                            Main Start
            *************************************-->
            <main id="tg-main" class="tg-main tg-haslayout">
                <!--************************************
                                Authors Start
                *************************************-->
                <div class="tg-authorsgrid">
                    <div class="container">
                        <div class="row">
                            <div class="tg-authors">
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                    <div class="tg-sectionhead">
                                        <h2><span>Strong Minds Behind Us</span>Most Popular Authors</h2>
                                    </div>
                                </div>


                                <c:forEach var="listAuthor" items="${listAuthor}">
                                    <div class="col-xs-6 col-sm-4 col-md-3 col-lg-2">
                                        <div class="tg-author">
                                            <figure><a href="javascript:void(0);"><img src="${listAuthor.getImage()}" alt="image description"></a></figure>
                                            <div class="tg-authorcontent">
                                                <h2><a href="list-book?authorId=${listAuthor.getAuthorId()}">${listAuthor.getAuthorName()}</a></h2>
                                                <span>${listAuthor.getDescription()}</span>
                                                <ul class="tg-socialicons">
                                                    <li class="tg-facebook"><a href="javascript:void(0);"><i class="fa fa-facebook"></i></a></li>
                                                    <li class="tg-twitter"><a href="javascript:void(0);"><i class="fa fa-twitter"></i></a></li>
                                                    <li class="tg-linkedin"><a href="javascript:void(0);"><i class="fa fa-linkedin"></i></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>

                                </c:forEach>

                            </div>
                        </div>
                    </div>
                </div>
                <!--************************************
                                Authors End
                *************************************-->
                <!--************************************
                                Testimonials Start
                *************************************-->
                <section class="tg-parallax tg-bgtestimonials tg-haslayout" data-z-index="-100" data-appear-top-offset="600" data-parallax="scroll" data-image-src="images/parallax/bgparallax-05.jpg">
                    <div class="tg-sectionspace tg-haslayout">
                        <div class="container">
                            <div class="row">
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-8 col-lg-push-2">
                                    <div id="tg-testimonialsslider" class="tg-testimonialsslider tg-testimonials owl-carousel">
                                        <div class="item tg-testimonial">
                                            <figure><img src="images/author/imag-02.jpg" alt="image description"></figure>
                                            <blockquote><q>Consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore tolore magna aliqua enim ad minim veniam, quis nostrud exercitation ullamcoiars nisi ut aliquip commodo.</q></blockquote>
                                            <div class="tg-testimonialauthor">
                                                <h3>Holli Fenstermacher</h3>
                                                <span>Manager @ CIFP</span>
                                            </div>
                                        </div>
                                        <div class="item tg-testimonial">
                                            <figure><img src="images/author/imag-02.jpg" alt="image description"></figure>
                                            <blockquote><q>Consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore tolore magna aliqua enim ad minim veniam, quis nostrud exercitation ullamcoiars nisi ut aliquip commodo.</q></blockquote>
                                            <div class="tg-testimonialauthor">
                                                <h3>Holli Fenstermacher</h3>
                                                <span>Manager @ CIFP</span>
                                            </div>
                                        </div>
                                        <div class="item tg-testimonial">
                                            <figure><img src="images/author/imag-02.jpg" alt="image description"></figure>
                                            <blockquote><q>Consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore tolore magna aliqua enim ad minim veniam, quis nostrud exercitation ullamcoiars nisi ut aliquip commodo.</q></blockquote>
                                            <div class="tg-testimonialauthor">
                                                <h3>Holli Fenstermacher</h3>
                                                <span>Manager @ CIFP</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

            </main>
            <!--************************************
                            Main End
            *************************************-->
            <!--************************************
                            Footer Start
            *************************************-->
            <jsp:include page="footer.jsp" />
            <!--************************************
                            Footer End
            *************************************-->
        </div>
        <!--************************************
                        Wrapper End
        *************************************-->
        <script src="js/vendor/jquery-library.js"></script>
        <script src="js/vendor/bootstrap.min.js"></script>
        <script src="https://maps.google.com/maps/api/js?key=AIzaSyCR-KEWAVCn52mSdeVeTqZjtqbmVJyfSus&amp;language=en"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/jquery.vide.min.js"></script>
        <script src="js/countdown.js"></script>
        <script src="js/jquery-ui.js"></script>
        <script src="js/parallax.js"></script>
        <script src="js/countTo.js"></script>
        <script src="js/appear.js"></script>
        <script src="js/gmap3.js"></script>
        <script src="js/main.js"></script>
    </body>

</html>