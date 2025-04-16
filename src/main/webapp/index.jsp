<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="el">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaTrade - Pharmacy Trading Platform</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }

        body {
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa;
        }

        .wrapper {
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .hero-section {
            background: linear-gradient(rgba(0, 0, 0, 0.45), rgba(0, 0, 0, 0.45)), url(./img/pillsimage.jpg) #000;
            background-size: cover;
            background-position: center;
            color: white;
            padding: 100px 0;
            flex: 1;
        }

        .feature-icon {
            font-size: 2.5rem;
            color: #0d6efd;
            margin-bottom: 1rem;
        }

        .btn-login {
            padding: 10px 30px;
            font-weight: 600;
        }

        footer {
            background-color: #343a40;
            color: white;
            padding: 20px 0;
            width: 100%;
        }

        .content {
            flex: 1;
            overflow-y: auto;
        }

        .hero-section h1,
        .hero-section .lead {
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.8);
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg" style="background-color: #b9dae1;">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                    <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo" style="height: 50px;">
                </a>

                <div class="ms-auto">
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-login">Log In</a>
                </div>
            </div>
        </nav>

        <!-- Content Area -->
        <div class="content">
            <!-- Hero Section -->
            <section class="hero-section text-center">
                <div class="container">
                    <h2 class="display-4 fw-bold mb-4">Διαχείριση ανταλλαγών φαρμακείων</h2>
                    <p class="lead mb-5">Ασφαλής πλατφόρμα για την καθημερινη διαχείριση ανταλλαγών μεταξύ φαρμακείων</p>
                    <div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg px-5 gap-4">Είσοδος</a>
                        <a href="#features" class="btn btn-outline-light btn-lg px-2">Μάθετε περισσότερα</a>
                    </div>
                </div>
            </section>

            <!-- Features Section -->
            <section id="features" class="py-4">
                <div class="container">
                    <h2 class="text-center mb-4">Βασικές Λειτουργείες</h2>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <div class="feature-icon">
                                        <i class="bi bi-arrow-left-right"></i>
                                    </div>
                                    <h3 class="h4">Διαχείριση ανταλλαγών</h3>
                                    <p>Κρατείστε εύκολα πλήρες ιστορικό των ανταλλαγών σας</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <div class="feature-icon">
                                        <i class="bi bi-shield-lock"></i>
                                    </div>
                                    <h3 class="h4">Ασφαλής Πλατφόρμα</h3>
                                    <p>Η Role-based πρόσβαση εξασφαλίζει την ασφάλεια των δεδομένων σας</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <div class="feature-icon">
                                        <i class="bi bi-graph-up"></i>
                                    </div>
                                    <h3 class="h4">Balance Tracking</h3>
                                    <p>Υπολογίζεται αυτόματα το τρέχων υπόλοιπο μεταξύ δυο συνεργατών</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <!-- Footer -->
        <footer class="text-center">
            <div class="container">
                <p class="mb-0">© 2025 Coding Factory. All rights reserved.</p>
            </div>
        </footer>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</body>
</html>