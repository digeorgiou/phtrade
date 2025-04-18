<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaTrade - Login</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg">
            <div class="container-fluid d-flex justify-content-between
            align-items-center ms-3">
                <!-- Logo -->
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                    <img src="${pageContext.request.contextPath}/img/pharmalogo.png"
                         alt="PharmaTrade Logo"
                         style="height: 50px;">
                </a>

                <!-- Welcome Message (centered) -->
                <div class="position-absolute start-50 translate-middle-x">
                    <span class="navbar-text fw-bold fs-4" style="color: #343a40;">
                        Καλώς ήρθατε στην Εφαρμογή
                    </span>
                </div>
            </div>
        </nav>

    <!-- Main Login Form -->
    <div class="container">
        <div class="login-container">
            <div class="login-logo">
                <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo">
            </div>

            <h2 class="text-center mb-4">Σύνδεση</h2>

            <%-- Display error message if authentication fails --%>
            <c:if test="${not empty error}">
                <div class="error-message">
                    <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                <!-- Username Field -->
                <div class="mb-3">
                    <label for="username" class="form-label">Όνομα Χρήστη</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                        <input type="text" class="form-control ${not empty usernameMessage ? 'is-invalid' : ''}"
                               id="username" name="username" required
                               value="${not empty requestScope.username ? requestScope.username : ''}">
                    </div>
                    <c:if test="${not empty usernameMessage}">
                        <div id = "username-error" class="invalid-feedback d-block">
                            <i class="bi bi-exclamation-circle-fill"></i>
                            <small>${requestScope.usernameMessage}</small>
                        </div>
                    </c:if>
                </div>

                <!-- Password Field -->
                <div class="mb-3">
                    <label for="password" class="form-label">Κωδικός</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password" class="form-control ${not empty passwordMessage ? 'is-invalid' : ''}"
                               id="password" name="password" required>
                        <button class="btn btn-outline-secondary toggle-password" type="button">
                            <i class="bi bi-eye-fill"></i>
                        </button>
                    </div>
                    <c:if test="${not empty passwordMessage}">
                        <div class="invalid-feedback d-block">
                            <i class="bi bi-exclamation-circle-fill"></i>
                            <small>${requestScope.passwordMessage}</small>
                        </div>
                    </c:if>

                    <!-- Forgot Password Link -->
                    <div class="text-end mt-2 auth-links">
                        <a href="#" class="text-decoration-none" data-bs-toggle="modal" data-bs-target="#forgotPasswordModal">
                        Ξεχάσατε τον κωδικό σας? </a>
                    </div>
                </div>

                <!-- Login Button -->
                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-primary btn-lg">Σύνδεση</button>
                </div>

                <!-- Register Link -->
                <div class="text-center auth-links">
                    <span class="text-muted">Δεν έχετε λογαριασμό ακόμα? </span>
                    <a href="${pageContext.request.contextPath}/register" class="text-decoration-none ms-1">Εγγραφή εδώ</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p class="mb-0">© 2025 Coding Factory. All rights reserved.</p>
        </div>
    </footer>

    <!-- Forgot Password Modal -->
    <div class="modal fade" id="forgotPasswordModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Password Reset</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="resetForm">
                        <div class="mb-3">
                            <label for="resetUsername" class="form-label">Enter your username</label>
                            <input type="text" class="form-control" id="resetUsername" name="username" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Generate Reset Link</button>
                        </div>
                    </form>

                    <!-- This will appear after form submission -->
                    <div id="resetLinkContainer" class="mt-3 d-none">
                        <div class="alert alert-info">
                            <p>Copy this link to reset your password (expires in 15 minutes):</p>
                            <div class="input-group mb-3">
                                <input type="text" id="resetLink" class="form-control" readonly>
                                <button class="btn btn-outline-secondary" onclick="copyResetLink()">
                                    <i class="bi bi-clipboard"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS  -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>