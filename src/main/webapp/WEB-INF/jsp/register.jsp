<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaTrade - Register</title>
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

        .register-container {
            max-width: 500px;
            margin: auto;
            padding: 2rem;
            background-color: #b9dae1;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            margin-top: 3rem;
            margin-bottom: 3rem;
        }

        .register-logo {
            text-align: center;
            margin-bottom: 2rem;
        }

        .register-logo img {
            height: 80px;
        }

        .form-control {
            background-color: rgba(255, 255, 255, 0.8);
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

        .error-message {
            color: #dc3545;
            margin-bottom: 1rem;
            text-align: center;
        }

        .password-strength {
            height: 5px;
            margin-top: 5px;
            background-color: #e9ecef;
            border-radius: 3px;
            overflow: hidden;
        }

        .password-strength-bar {
            height: 100%;
            width: 0%;
            transition: width 0.3s ease;
        }

        .form-text {
            font-size: 0.85rem;
        }

        .password-match {
            font-size: 0.85rem;
            margin-top: 5px;
        }

        .valid-feedback, .invalid-feedback {
            font-size: 0.85rem;
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

    <!-- Main Registration Form -->
    <div class="container">
        <div class="register-container">
            <h2 class="text-center mb-4">Create Account</h2>

            <%-- Display error message if registration fails --%>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                </div>
            </c:if>

            document.getElementById('registerForm').addEventListener('submit', function(e) {
                // Clear previous server errors
                document.querySelectorAll('.server-error').forEach(el => el.remove());

                // Client-side validation
                if (!this.checkValidity()) {
                    e.preventDefault();
                    this.classList.add('was-validated');
                }
            });

                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="invalid-feedback">Please provide a valid email address</div>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password" class="form-control is-invalid"
                        id="password" name="password"
                               required minlength="8" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$">
                        <button class="btn btn-outline-secondary toggle-password" type="button">
                            <i class="bi bi-eye-fill"></i>
                        </button>
                    </div>
                    <div class="password-strength">
                        <div class="password-strength-bar"></div>
                    </div>
                    <div class="form-text">
                        Minimum 8 characters with at least 1 uppercase, 1 lowercase, and 1 number
                    </div>
                    <div class="invalid-feedback">
                        Password must be at least 8 characters with uppercase, lowercase, and number
                    </div>
                </div>

                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>
                        <input type="password" class="form-control" id="confirmPassword"
                               required minlength="8">
                        <button class="btn btn-outline-secondary toggle-password" type="button">
                            <i class="bi bi-eye-fill"></i>
                        </button>
                    </div>
                    <div class="password-match text-success" id="passwordMatch" style="display: none;">
                        <i class="bi bi-check-circle-fill"></i> Passwords match
                    </div>
                    <div class="password-match text-danger" id="passwordMismatch" style="display: none;">
                        <i class="bi bi-exclamation-circle-fill"></i> Passwords don't match
                    </div>
                    <div class="invalid-feedback">Passwords must match</div>
                </div>

                <!-- Terms and Conditions -->
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="terms" required>
                    <label class="form-check-label" for="terms">
                        I agree to the <a href="#" data-bs-toggle="modal" data-bs-target="#termsModal">Terms and Conditions</a>
                    </label>
                    <div class="invalid-feedback">You must agree to the terms and conditions</div>
                </div>

                <!-- Register Button -->
                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-primary btn-lg">Register</button>
                </div>

                <!-- Login Link -->
                <div class="text-center auth-links">
                    <span class="text-muted">Already have an account?</span>
                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none ms-1">Login here</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p class="mb-0">© 2023 PharmaTrade. All rights reserved.</p>
        </div>
    </footer>

    <!-- Terms and Conditions Modal -->
    <div class="modal fade" id="termsModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Terms and Conditions</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h6>1. Acceptance of Terms</h6>
                    <p>By registering for an account with PharmaTrade, you agree to be bound by these Terms and Conditions...</p>

                    <h6>2. User Responsibilities</h6>
                    <p>You are responsible for maintaining the confidentiality of your account and password...</p>

                    <h6>3. Privacy Policy</h6>
                    <p>Your personal information will be handled in accordance with our Privacy Policy...</p>

                    <h6>4. Prohibited Activities</h6>
                    <p>You agree not to use the service for any unlawful purpose or in any way that might harm...</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal">I Understand</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Form validation
            const form = document.getElementById('registerForm');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            const passwordMatch = document.getElementById('passwordMatch');
            const passwordMismatch = document.getElementById('passwordMismatch');
            const strengthBar = document.querySelector('.password-strength-bar');

            // Toggle password visibility
            document.querySelectorAll('.toggle-password').forEach(button => {
                button.addEventListener('click', function() {
                    const input = this.parentNode.querySelector('input');
                    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
                    input.setAttribute('type', type);
                    this.querySelector('i').classList.toggle('bi-eye-fill');
                    this.querySelector('i').classList.toggle('bi-eye-slash-fill');
                });
            });

            document.getElementById('username').addEventListener('input', function() {
              const isValid = this.value.length >= 4 && /^[a-zA-Z0-9]+$/.test(this.value);

              if (this.value.length > 0) {
                this.classList.toggle('is-invalid', !isValid);
              }
            });


            // Password strength indicator
            password.addEventListener('input', function() {
                const strength = calculatePasswordStrength(this.value);
                strengthBar.style.width = strength.percentage + '%';
                strengthBar.style.backgroundColor = strength.color;
            });

            // Password match checking
            confirmPassword.addEventListener('input', function() {
                if (password.value && this.value) {
                    if (password.value === this.value) {
                        passwordMatch.style.display = 'block';
                        passwordMismatch.style.display = 'none';
                        confirmPassword.setCustomValidity('');
                    } else {
                        passwordMatch.style.display = 'none';
                        passwordMismatch.style.display = 'block';
                        confirmPassword.setCustomValidity('Passwords must match');
                    }
                } else {
                    passwordMatch.style.display = 'none';
                    passwordMismatch.style.display = 'none';
                }
            });

            // Form submission
            document.getElementById('registerForm').addEventListener('submit', function(e) {
              // Manually check all fields
              const username = document.getElementById('username');
              const isUsernameValid = username.value.length >= 4;

              if (!isUsernameValid) {
                e.preventDefault();
                username.classList.add('is-invalid');
                this.classList.add('was-validated'); // <-- This activates messages
              }
            });

            // Calculate password strength
            function calculatePasswordStrength(password) {
                let strength = 0;

                // Length contributes up to 50%
                strength += Math.min(50, (password.length / 12) * 50);

                // Mixed case contributes up to 20%
                if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) {
                    strength += 20;
                }

                // Numbers contribute up to 20%
                if (password.match(/([0-9])/)) {
                    strength += 20;
                }

                // Special characters contribute up to 10%
                if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) {
                    strength += 10;
                }

                // Determine color based on strength
                let color;
                if (strength < 40) {
                    color = '#dc3545'; // Red
                } else if (strength < 70) {
                    color = '#fd7e14'; // Orange
                } else {
                    color = '#28a745'; // Green
                }

                return {
                    percentage: strength,
                    color: color
                };
            }
        });
    </script>
</body>
</html>