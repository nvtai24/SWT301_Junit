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
            #addAuthorForm {
                display: none;
            }
            /* Nút thêm tác giả */
            #showFormBtn {
                margin-bottom: 10px;
                background-color: #77b748; /* Màu xanh lá cây */
                color: white;
                padding: 10px 20px;
                font-size: 1em;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }

            #showFormBtn:hover {
                background-color: #77b748;
            }

            button[type="submit"] {
                background-color: #77b748; /* Màu xanh lá cây */
                color: white;
                padding: 10px 20px;
                font-size: 1.1em;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-bottom: 10px;
                margin-top: 10px;

            }

            button[type="submit"]:hover {
                background-color: #77b748; /* Màu xanh lá cây tối */
            }

            /* Khung bảng */
            table {
                width: 100%;
                margin-top: 30px;
                border-collapse: collapse;
            }

            table th, table td {
                padding: 12px;
                text-align: center;
                border: 1px solid #ddd;
            }

            table th {
                background-color: #77b748; /* Màu xanh lá cây */
                color: white;
            }

            table tbody tr:nth-child(even) {
                background-color: #f2f2f2;
            }

            /* Nút hành động trong bảng */
            .btn-sm {
                padding: 5px 10px;
                font-size: 0.9em;
                margin-right: 5px;
            }

            .btn-warning {
                background-color: #f39c12;
                color: white;
            }

            .btn-warning:hover {
                background-color: #e67e22;
            }

            .btn-danger {
                background-color: #e74c3c;
                color: white;
            }

            .btn-danger:hover {
                background-color: #c0392b;
            }


            /* Form Styling */
            .author-form-container {
                margin: 30px 0;
                padding: 25px;
                background-color: #f8f9fa;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .author-form {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
                align-items: flex-start;
            }

            .form-group {
                flex: 1 1 300px;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 600;
                color: #333;
            }

            .form-control {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 16px;
                transition: border-color 0.3s;
            }

            .form-control:focus {
                border-color: #77b748;
                outline: none;
                box-shadow: 0 0 0 3px rgba(119, 183, 72, 0.2);
            }

            .image-preview-container {
                margin-top: 10px;
                width: 150px;
                height: 150px;
                border: 2px dashed #ddd;
                border-radius: 4px;
                display: flex;
                justify-content: center;
                align-items: center;
                overflow: hidden;
                background-color: #fff;
            }

            .image-preview {
                max-width: 100%;
                max-height: 100%;
                display: none;
            }

            .image-placeholder {
                color: #999;
                font-size: 14px;
                text-align: center;
            }

            .btn-submit {
                background-color: #77b748;
                color: white;
                padding: 12px 24px;
                font-size: 16px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s;
                margin-top: 20px;
            }

            .btn-submit:hover {
                background-color: #669e3e;
            }

            /* Responsive adjustments */
            @media (max-width: 768px) {
                .author-form {
                    flex-direction: column;
                }

                .form-group {
                    flex: 1 1 100%;
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
                                <h1>Manager Author</h1>
                                <ol class="tg-breadcrumb">
                                    <li><a href="home">home</a></li>
                                    <li><a href="javascript:void(0);">Manager</a></li>
                                    <li class="tg-active">Manager Author</li>
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
                                News Grid Start
                *************************************-->
                <div class="tg-sectionspace tg-haslayout">
                    <div class="container">
                        <div class="row">
                            <div class="container">

                                <!-- Button to show the form -->
                                <button id="showFormBtn" class="btn btn-success mb-3">Add New Author</button>

                                <!-- Add Author Form -->
                                <form id="addAuthorForm" action="add-edit-author" method="post" enctype="multipart/form-data" class="author-form">
                                    <input type="hidden" name="action" value="add">

                                    <div class="form-group">
                                        <label for="authorName">Author Name</label>
                                        <input type="text" class="form-control" name="authorName" id="authorName" placeholder="Enter author name" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="authorImageFile">Author Image</label>
                                        <input type="file" class="form-control" name="authorImageFile" id="authorImageFile" accept="image/*">
                                        <div class="image-preview-container">
                                            <img id="imagePreview" class="image-preview" src="#" alt="Image Preview">
                                            <div id="imagePlaceholder" class="image-placeholder">
                                                <i class="fa fa-image"></i>
                                                <p>Image preview will appear here</p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="authorDescription">Description</label>
                                        <textarea class="form-control" name="authorDescription" id="authorDescription" rows="4" placeholder="Enter author description"></textarea>
                                    </div>

                                    <div class="form-group" style="flex-basis: 100%;">
                                        <button type="submit" class="btn-submit">Add New Author</button>
                                    </div>
                                </form>

                                <!-- Edit Author Form -->
                                <div class="author-form-container" id="editAuthorFormContainer" style="display: none;">
                                    <h3>Edit Author</h3>
                                    <form id="editAuthorForm" action="add-edit-author" method="post" enctype="multipart/form-data" class="author-form">
                                        <input type="hidden" name="action" value="edit">
                                        <input type="hidden" name="authorId" id="editAuthorId">

                                        <div class="form-group">
                                            <label for="editAuthorName">Author Name</label>
                                            <input type="text" class="form-control" name="authorName" id="editAuthorName" placeholder="Enter author name" required>
                                        </div>

                                        <div class="form-group">
                                            <label for="editAuthorImageFile">Author Image</label>
                                            <input type="file" class="form-control" name="authorImageFile" id="editAuthorImageFile" accept="image/*">
                                            <div class="image-preview-container">
                                                <img id="editImagePreview" class="image-preview" src="#" alt="Image Preview">
                                                <div id="editImagePlaceholder" class="image-placeholder">
                                                    <i class="fa fa-image"></i>
                                                    <p>Current or new image preview</p>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="editAuthorDescription">Description</label>
                                            <textarea class="form-control" name="authorDescription" id="editAuthorDescription" rows="4" placeholder="Enter author description"></textarea>
                                        </div>

                                        <div class="form-group" style="flex-basis: 100%;">
                                            <button type="submit" class="btn-submit">Update Author</button>
                                        </div>
                                    </form>
                                </div>
                                <table id="order-list" class="display table table-striped table-hover tg-order-table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Name</th>
                                            <th>Image</th>
                                            <th>Description</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="listAuthor" items="${listAuthor}">
                                            <tr>
                                                <td>${listAuthor.authorId}</td>
                                                <td>${listAuthor.authorName}</td>
                                                <td><img src="${listAuthor.image}" alt="Ảnh" width="50"></td>
                                                <td>${listAuthor.description}</td>
                                                <td>
                                                    <button type="button" class="btn btn-warning btn-sm" 
                                                            onclick="showEditForm('${listAuthor.authorId}', '${listAuthor.authorName}', '${listAuthor.image}', '${listAuthor.description}')">
                                                        Edit
                                                    </button>
                                                    <a href="delete?authorId=${listAuthor.authorId}&delete=deleteAuthor" class="btn btn-danger btn-sm" 
                                                       onclick="return confirm('Bạn có chắc chắn muốn xóa tác giả này?');">Delete</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="card-footer">
                            <c:if test="${totalPages > 1}">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="managerUser?page=1">First</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="managerUser?page=${currentPage - 1}">Previous</a>
                                        </li>
                                    </c:if>

                                    <c:forEach begin="1" end="${totalPages}" varStatus="loop">
                                        <c:choose>
                                            <c:when test="${loop.index == currentPage}">
                                                <li class="page-item active">
                                                    <span class="page-link">${loop.index}</span>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="page-item">
                                                    <a class="page-link" href="manager-user?page=${loop.index}">${loop.index}</a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="manager-user?page=${currentPage + 1}">Next</a>
                                        </li>
                                        <li class="page-item">
                                            <a class="page-link" href="manager-user?page=${totalPages}">Last</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </c:if>
                        </div>
                    </div>
                </div>
                <!--************************************
                                News Grid End
                *************************************-->
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

        <script>
                                                           // JavaScript to show/hide the form
                                                           document.getElementById("showFormBtn").addEventListener("click", function () {
                                                               var form = document.getElementById("addAuthorForm");
                                                               if (form.style.display === "none" || form.style.display === "") {
                                                                   form.style.display = "block";
                                                               } else {
                                                                   form.style.display = "none";
                                                               }
                                                           });

                                                           // JavaScript to show/hide the edit form and populate it with data
                                                           function showEditForm(authorId, authorName, authorImage, authorDescription) {
                                                               var form = document.getElementById("editAuthorForm");
                                                               form.style.display = "block";
                                                               document.getElementById("editAuthorId").value = authorId;
                                                               document.getElementById("editAuthorName").value = authorName;
                                                               document.getElementById("editAuthorImage").value = authorImage;
                                                               document.getElementById("editAuthorDescription").value = authorDescription;
                                                           }

                                                           function showEditForm(authorId, authorName, authorImage, authorDescription) {
                                                               var form = document.getElementById("editAuthorForm");
                                                               form.style.display = "block";
                                                               document.getElementById("editAuthorId").value = authorId;
                                                               document.getElementById("editAuthorName").value = authorName;
                                                               document.getElementById("editAuthorDescription").value = authorDescription;

                                                               // Display the current image if there is one
                                                               var currentImageElement = document.getElementById("currentAuthorImage");
                                                               if (authorImage && authorImage !== '') {
                                                                   currentImageElement.src = authorImage;
                                                                   currentImageElement.style.display = "block";
                                                               } else {
                                                                   currentImageElement.style.display = "none";
                                                               }
                                                           }
                                                           document.getElementById('authorImageFile').addEventListener('change', function (event) {
                                                               const imagePreview = document.getElementById('imagePreview');
                                                               const imagePlaceholder = document.getElementById('imagePlaceholder');

                                                               if (event.target.files && event.target.files[0]) {
                                                                   const reader = new FileReader();

                                                                   reader.onload = function (e) {
                                                                       imagePreview.src = e.target.result;
                                                                       imagePreview.style.display = 'block';
                                                                       imagePlaceholder.style.display = 'none';
                                                                   };

                                                                   reader.readAsDataURL(event.target.files[0]);
                                                               } else {
                                                                   imagePreview.style.display = 'none';
                                                                   imagePlaceholder.style.display = 'flex';
                                                               }
                                                           });

                                                           document.getElementById('editAuthorImageFile').addEventListener('change', function (event) {
                                                               const imagePreview = document.getElementById('editImagePreview');
                                                               const imagePlaceholder = document.getElementById('editImagePlaceholder');

                                                               if (event.target.files && event.target.files[0]) {
                                                                   const reader = new FileReader();

                                                                   reader.onload = function (e) {
                                                                       imagePreview.src = e.target.result;
                                                                       imagePreview.style.display = 'block';
                                                                       imagePlaceholder.style.display = 'none';
                                                                   };

                                                                   reader.readAsDataURL(event.target.files[0]);
                                                               }
                                                           });


                                                           function showEditForm(authorId, authorName, authorImage, authorDescription) {
                                                               document.getElementById('editAuthorFormContainer').style.display = 'block';

                                                               document.getElementById('editAuthorId').value = authorId;
                                                               document.getElementById('editAuthorName').value = authorName;
                                                               document.getElementById('editAuthorDescription').value = authorDescription;

                                                               const imagePreview = document.getElementById('editImagePreview');
                                                               const imagePlaceholder = document.getElementById('editImagePlaceholder');

                                                               if (authorImage && authorImage !== '') {
                                                                   imagePreview.src = authorImage;
                                                                   imagePreview.style.display = 'block';
                                                                   imagePlaceholder.style.display = 'none';
                                                               } else {
                                                                   imagePreview.style.display = 'none';
                                                                   imagePlaceholder.style.display = 'flex';
                                                               }

                                                               document.getElementById('editAuthorFormContainer').scrollIntoView({
                                                                   behavior: 'smooth'
                                                               });
                                                           }
        </script>

    </body>
</html>
