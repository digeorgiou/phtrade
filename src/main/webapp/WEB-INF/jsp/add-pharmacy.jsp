<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaTrade - Add Pharmacy</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
    /* General Styles */
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    body {
        display: flex;
        flex-direction: column;
        background-color: #f8f9fa;
    }

    /* Navbar Styles - Matching Dashboard */
    .navbar {
        background-color: #b9dae1 !important;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        padding: 0.5rem 0;
    }

    .navbar-brand img {
        height: 50px;
        transition: transform 0.3s ease;
    }

    .navbar-brand img:hover {
        transform: scale(1.05);
    }

    .navbar-text {
        color: #343a40;
        font-weight: 500;
    }

    /* Main Content Container */
    .container {
        flex: 1;
        padding-top: 2rem;
        padding-bottom: 2rem;
    }

    /* Card Styles - Matching Dashboard */
    .card {
        border: none;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    }

    .card-title {
        color: #343a40;
        font-weight: 600;
    }

    /* Form Styles */
    .form-control {
        border-radius: 5px;
        padding: 0.75rem 1rem;
        border: 1px solid #ced4da;
        transition: border-color 0.3s ease, box-shadow 0.3s ease;
    }

    .form-control:focus {
        border-color: #b9dae1;
        box-shadow: 0 0 0 0.25rem rgba(185, 218, 225, 0.25);
    }

    .input-group-text {
        background-color: #e9ecef;
        border-color: #ced4da;
    }

    /* Button Styles - Matching Dashboard */
    .btn {
        border-radius: 5px;
        padding: 0.5rem 1.25rem;
        font-weight: 500;
        transition: all 0.3s ease;
    }

    .btn-primary {
        background-color: #0d6efd;
        border-color: #0d6efd;
    }

    .btn-primary:hover {
        background-color: #0b5ed7;
        border-color: #0a58ca;
    }

    .btn-outline-secondary {
        color: #6c757d;
        border-color: #6c757d;
    }

    .btn-outline-secondary:hover {
        background-color: #6c757d;
        color: white;
    }

    .btn-lg {
        padding: 0.75rem 1.5rem;
        font-size: 1.1rem;
    }

    /* Alert Styles */
    .alert {
        border-radius: 5px;
        padding: 1rem;
    }

    .alert-success {
        background-color: #d1e7dd;
        border-color: #badbcc;
        color: #0f5132;
    }

    .alert-danger {
        background-color: #f8d7da;
        border-color: #f5c2c7;
        color: #842029;
    }

    /* Validation Error Styles */
    .invalid-feedback {
        color: #dc3545;
        font-size: 0.875rem;
    }

    .is-invalid {
        border-color: #dc3545;
    }

    .is-invalid:focus {
        border-color: #dc3545;
        box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
    }

    /* Footer Styles - Matching Dashboard */
    footer {
        background-color: #343a40;
        color: white;
        padding: 1.5rem 0;
        margin-top: auto;
    }

    footer p {
        margin-bottom: 0;
    }

    /* Responsive Adjustments */
    @media (max-width: 768px) {
        .navbar-text {
            font-size: 1rem;
        }

        .card {
            margin-top: 1rem;
        }
    }

    /* Animation for form elements */
    .form-group {
        transition: all 0.3s ease;
    }

    /* Icon styles - Matching Dashboard */
    .bi {
        vertical-align: middle;
        margin-right: 5px;
    }

    /* Custom styles for the add-pharmacy page */
    .add-pharmacy-container {
        max-width: 600px;
        margin: 0 auto;
    }

    /* Balance card styles - Matching Dashboard */
    .balance-card {
        background-color: #b9dae1;
        border-radius: 10px;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        transition: transform 0.3s ease;
    }

    .balance-card:hover {
        transform: translateY(-3px);
    }

    .positive-balance {
        color: #198754;
    }

    .negative-balance {
        color: #dc3545;
    }

    /* Sidebar styles - Matching Dashboard */
    .sidebar {
        background-color: #e9ecef;
        box-shadow: 2px 0 5px rgba(0,0,0,0.1);
        overflow-y: auto;
    }

    .sidebar .nav-link {
        color: #495057;
        border-radius: 5px;
        margin-bottom: 5px;
        padding: 0.5rem 1rem;
        transition: all 0.3s ease;
    }

    .sidebar .nav-link:hover {
        background-color: #dee2e6;
        color: #343a40;
    }

    .sidebar .nav-link.active {
        background-color: #b9dae1;
        color: #343a40;
        font-weight: 500;
    }

    .sidebar .nav-link i {
        margin-right: 8px;
    }

    /* Main content area - Matching Dashboard */
    .main-content {
        padding: 20px;
        background-color: white;
        border-radius: 5px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }
    </style>
</head>
<body>
    <!-- Navigation - Same as dashboard -->
    <nav class="navbar navbar-expand-lg">
        <div class="container-fluid d-flex justify-content-between align-items-center ms-3 me-3">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo" style="height: 50px;">
            </a>
            <div class="position-absolute start-50 translate-middle-x">
                <span class="navbar-text fw-bold fs-4" style="color: #343a40;">Add New Pharmacy</span>
            </div>
            <div class="dropdown">
                <button class="btn btn-outline-secondary dropdown-toggle" type="button" id="userDropdown" data-bs-toggle="dropdown">
                    <i class="bi bi-person-circle"></i> ${sessionScope.username}
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="#"><i class="bi bi-gear"></i> Settings</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right"></i> Logout</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h3 class="card-title text-center mb-4">Add New Pharmacy</h3>

                        <%-- Display success message if redirected from successful addition --%>
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="bi bi-check-circle-fill"></i> ${successMessage}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <c:remove var="successMessage" scope="request"/> <%-- Clear after displaying --%>
                        </c:if>

                        <%-- Display error message if any --%>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/add-pharmacy" method="POST">
                            <!-- Pharmacy Name Field -->
                            <div class="mb-3">
                                <label for="name" class="form-label">Pharmacy Name</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-shop"></i></span>
                                    <input type="text" class="form-control ${not empty nameMessage ? 'is-invalid' : ''}"
                                           id="name" name="name" required
                                           value="${not empty requestScope.name ? requestScope.name : ''}">
                                </div>
                                <c:if test="${not empty nameMessage}">
                                    <div class="invalid-feedback d-block">
                                        <i class="bi bi-exclamation-circle-fill"></i>
                                        <small>${requestScope.nameMessage}</small>
                                    </div>
                                </c:if>
                            </div>

                            <!-- Submit Button -->
                            <div class="d-grid gap-2 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="bi bi-plus-circle"></i> Add Pharmacy
                                </button>
                                <a href="${pageContext.request.contextPath}/pharmapp/dashboard" class="btn btn-outline-secondary">
                                    <i class="bi bi-arrow-left"></i> Back to Dashboard
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="mt-auto">
        <div class="container text-center">
            <p class="mb-0">© 2025 Coding Factory. All rights reserved.</p>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>