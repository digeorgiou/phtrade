<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PharmaTrade - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        html, body {
            height: 100%;
            padding: 0;
            margin: 0;
        }

        body {
            display: flex;
            flex-direction: column;
            background-color: #f8f9fa;
        }

        .navbar {
            background-color: #b9dae1 !important;
        }

        .container-fluid {
            flex: 1;
            display: flex;
            flex-direction: column;
        }

        .row.h-85 {
            flex: 1;
            min-height: 0; /* Fix for Firefox */
            margin: 0;
            overflow-y: auto; /* Add scroll to content if needed */
        }

        .sidebar {
            background-color: #e9ecef;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            overflow-y: auto;
        }

        .main-content {
            padding: 20px;
            background-color: white;
            overflow-y: auto;
        }

        footer {
            background-color: #343a40;
            color: white;
            padding: 1.5rem 0;
            flex-shrink: 0;
        }

        .bg-light-green {
                background-color: #e8f5e9;  /* Very light green */
            }
            .bg-light-red {
                background-color: #ffebee;  /* Very light red */
            }

        .sidebar .nav-link { color: #495057; border-radius: 5px; margin-bottom: 5px; }
        .sidebar .nav-link:hover { background-color: #dee2e6; }
        .sidebar .nav-link.active { background-color: #b9dae1; color: #343a40; font-weight: 500; }
        .balance-card { background-color: #b9dae1; border-radius: 10px; padding: 20px; margin-bottom: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        .positive-balance { color: #198754; }
        .negative-balance { color: #dc3545; }

        .balance-card {
            background-color: #b9dae1;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                height: 400px;
        }

        .recent-trades {
            border: 1px solid #dee2e6;
                border-radius: 5px;
                padding: 5px;
                min-height: 150px;
        }

        .list-group-item {
            border-left: none;
            border-right: none;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class = "container-fluid">
            <div class="d-flex justify-content-between w-100 align-items-center
            ms-3 me-3">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                    <img src="${pageContext.request.contextPath}/img/pharmalogo.png" alt="PharmaTrade Logo" style="height: 50px;">
                </a>
                <div class="position-absolute start-50 translate-middle-x">
                    <span class="navbar-text fw-bold fs-4" style="color: #343a40;">PharmaTrade Dashboard</span>
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
        </div>
    </nav>

    <div class="container-fluid px-0">
        <div class="row h-85">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 sidebar">
                <div class="sidebar-sticky pt-3">
                    <h5 class="px-3">My Pharmacies</h5>
                    <div class="list-group list-group-flush mb-3">
                        <c:forEach items="${not empty user.pharmacies ? user.pharmacies : []}" var="pharmacy">
                            <a href="${pageContext.request.contextPath}/pharmapp/dashboard?pharmacyId=${pharmacy.id}"
                               class="list-group-item list-group-item-action ${param.pharmacyId eq pharmacy.id.toString() ? 'active' : ''}">
                                ${pharmacy.name}
                            </a>
                        </c:forEach>
                    </div>
                    <hr>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/add-contact">
                                <i class="bi bi-person-plus"></i> Add Contact
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/view-trades">
                                <i class="bi bi-list-check"></i> View Trades
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/add-pharmacy">
                                <i class="bi bi-shop"></i> Add Pharmacy
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10 main-content">
                <c:choose>
                    <c:when test="${not empty selectedPharmacy}">
                        <h2>${selectedPharmacy.name} - Balances</h2>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <!-- Simple Search Form -->
                                <form action="${pageContext.request.contextPath}/pharmapp/dashboard" method="GET" class="mb-3">
                                    <input type="hidden" name="pharmacyId" value="${selectedPharmacy.id}">
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="search"
                                               placeholder="Search contacts..." value="${not empty searchTerm ? searchTerm : ''}">
                                        <button class="btn btn-primary" type="submit">
                                            <i class="bi bi-search"></i> Search
                                        </button>
                                        <c:if test="${not empty searchTerm}">
                                            <a href="${pageContext.request.contextPath}/pharmapp/dashboard?pharmacyId=${selectedPharmacy.id}"
                                               class="btn btn-outline-secondary">
                                                <i class="bi bi-x"></i> Clear
                                            </a>
                                        </c:if>
                                    </div>
                                </form>
                            </div>
                            <div class="col-md-6">
                                <!-- Simple Sort Links -->
                                <div class="btn-group float-end">
                                    <a href="?pharmacyId=${selectedPharmacy.id}&search=${not empty searchTerm ? searchTerm : ''}&sort=name"
                                       class="btn btn-outline-primary ${currentSort == 'name' ? 'active' : ''}">
                                        A-Z
                                    </a>
                                    <a href="?pharmacyId=${selectedPharmacy.id}&search=${not empty searchTerm ? searchTerm : ''}&sort=name-desc"
                                       class="btn btn-outline-primary ${currentSort == 'name-desc' ? 'active' : ''}">
                                        Z-A
                                    </a>
                                    <a href="?pharmacyId=${selectedPharmacy.id}&search=${not empty searchTerm ? searchTerm : ''}&sort=trades"
                                       class="btn btn-outline-primary ${currentSort == 'trades' ? 'active' : ''}">
                                        Most Trades
                                    </a>
                                    <a href="?pharmacyId=${selectedPharmacy.id}&search=${not empty searchTerm ? searchTerm : ''}&sort=trades-desc"
                                       class="btn btn-outline-primary ${currentSort == 'trades-desc' ? 'active' : ''}">
                                        Fewest Trades
                                    </a>
                                </div>
                            </div>
                        </div>
                        <p class="text-muted">Current balances with your contacts</p>

                        <c:choose>
                            <c:when test="${not empty balanceList}">
                                <div class="row">
                                <c:if test="${empty balanceList}">
                                    <p>No balance data available</p>
                                </c:if>
                                    <c:forEach items="${balanceList}" var="balance">
                                        <c:if test="${balance != null}">
                                        <div class="col-md-6 col-lg-4 mb-4">
                                            <div class="balance-card h-100
                                            d-flex flex-column position-relative">
                                                <p class="position-absolute
                                                top-0 end-0 m-3"> <!-- Positioning classes -->
                                                    <small class="text-muted">Αριθμός
                                                    Συναλλαγών:
                                                    ${balance
                                                    .tradeCount}</small>
                                                </p>
                                                <div class="flex-grow-0">
                                                    <h4>${not empty balance
                                                    .contactName ? balance
                                                    .contactName : 'No contact name'}</h4>
                                                    <small class="text-muted">${not empty balance.pharmacyName ? balance.pharmacyName : 'Unknown pharmacy'}</small>
                                                    <h4 class="${balance.amount
                                                    < 0 ? 'text-success' :
                                                    (balance.amount > 0 ?
                                                    'text-danger' : '')} mt-1">
                                                        <c:choose>
                                                            <c:when test="${balance.amount < 0}">
                                                                ΜΑΣ ΧΡΩΣΤΑΝΕ €<fmt:formatNumber value="${-balance.amount}" minFractionDigits="2" maxFractionDigits="2"/>
                                                            </c:when>
                                                            <c:when test="${balance.amount > 0}">
                                                                ΧΡΩΣΤΑΜΕ €<fmt:formatNumber value="${balance.amount}" minFractionDigits="2" maxFractionDigits="2"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                €<fmt:formatNumber value="${balance.amount}" minFractionDigits="2" maxFractionDigits="2"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </h4>
                                                </div>

                                                <!-- Recent trades section -->
                                                <div class="recent-trades mt-1
                                                flex-grow-1 overflow-hidden d-flex flex-column";>
                                                    <h6
                                                    class="flex-grow-0">Πρόσφατες Συναλλαγές:</h6>
                                                    <ul class="list-group
                                                    list-group-flush flex-grow-1
                                                     overflow-auto" >
                                                        <c:forEach items="${balance.recentTrades}" var="trade">
                                                            <li class="list-group-item p-1 ${trade.outgoing ? 'bg-light-red' : 'bg-light-green'}">
                                                                <small>
                                                                    ${trade.formattedDate} - ${trade.description}
                                                                    <span class="float-end">
                                                                        €<fmt:formatNumber value="${trade.amount}" minFractionDigits="2"/>
                                                                        <i class="bi ${trade.outgoing ? 'bi-arrow-up text-danger' : 'bi-arrow-down text-success'}"></i>
                                                                    </span>
                                                                </small>
                                                            </li>
                                                        </c:forEach>
                                                    </ul>
                                                </div>

                                                <div class="d-flex
                                                justify-content-between mt-3 pt-2 border-top flex-grow-0">
                                                    <a href="${pageContext.request.contextPath}/record-trade?giverId=${selectedPharmacy.id}&receiverId=${balance.pharmacyId}"
                                                       class="btn btn-sm btn-outline-primary">
                                                        Record Trade
                                                    </a>
                                                    <h4>"${balance.tradeCount}"</h4>
                                                    <a href="${pageContext.request.contextPath}/view-trades?pharmacy1=${selectedPharmacy.id}&pharmacy2=${balance.pharmacyId}"
                                                       class="btn btn-sm btn-outline-secondary">
                                                        View History
                                                    </a>
                                                </div>
                                            </div>
                                        </div>

                                        </c:if>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    <i class="bi bi-info-circle"></i> You don't have any contacts yet.
                                    <a href="${pageContext.request.contextPath}/add-contact" class="alert-link">Add a contact</a> to start tracking trades.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex justify-content-center align-items-center" style="height: 70vh;">
                            <div class="text-center">
                                <i class="bi bi-shop" style="font-size: 3rem; color: #6c757d;"></i>
                                <h3 class="mt-3">Select a pharmacy to view balances</h3>
                                <p class="text-muted">Choose one of your pharmacies from the sidebar</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p class="mb-0">© 2025 Coding Factory. All rights reserved.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>