<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaTrade - Registration Successful</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        .navbar {
            background-color: #b9dae1 !important;
        }

        .success-container {
            max-width: 600px;
            margin: auto;
            padding: 2rem;
            background-color: #b9dae1;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            margin-top: 3rem;
            margin-bottom: 3rem;
            text-align: center;
        }

        .success-logo {
            text-align: center;
            margin-bottom: 2rem;
        }

        .success-logo img {
            height: 80px;
        }

        .success-icon {
            font-size: 4rem;
            color: #28a745;
            margin-bottom: 1.5rem;
        }

        .user-details {
            background-color: rgba(255, 255, 255, 0.8);
            border-radius: 8px;
            padding: 1.5rem;
            margin: 1.5rem 0;
            text-align: left;
        }

        .detail-row {
            display: flex;
            margin-bottom: 0.5rem;
        }

        .detail-label {
            font-weight: 600;
            min-width: 120px;
        }

        footer {
            background-color: #343a40;
            color: white;
            padding: 1.5rem 0;
            margin-top: auto;
        }

        .auth-links {
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo" style="height: 50px;">
            </a>
        </div>
    </nav>

    <!-- Success Content -->
    <div class="container">
        <div class="success-container">
            <div class="success-logo">
                <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo">
            </div>

            <div class="success-icon">
                <i class="bi bi-check-circle-fill"></i>
            </div>

            <h2 class="text-center mb-3">Registration Successful!</h2>
            <p class="text-center mb-4">Welcome to PharmaTrade. Your account has been created successfully.</p>

            <div class="user-details">
                <div class="detail-row">
                    <span class="detail-label">Username:</span>
                    <span>${registeredUser.username}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Email:</span>
                    <span>${registeredUser.email}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Account Type:</span>
                    <span>${registeredUser.role}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Status:</span>
                    <span class="text-success">Active <i class="bi bi-check-circle-fill"></i></span>
                </div>
            </div>

            <div class="d-grid gap-2">
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary btn-lg">
                    Go to Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-secondary">
                    Edit Profile
                </a>
            </div>

            <div class="text-center mt-4 auth-links">
                <span class="text-muted">Need help?</span>
                <a href="${pageContext.request.contextPath}/support" class="text-decoration-none ms-1">Contact Support</a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p class="mb-0">© 2023 PharmaTrade. All rights reserved.</p>
        </div>
    </footer>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>