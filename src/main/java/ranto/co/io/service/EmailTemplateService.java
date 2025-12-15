package ranto.co.io.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ranto.co.io.mail.EmailRequest;

import java.time.Duration;

/**
 * Service de gestion des modèles d'emails pour les communications utilisateur.
 */
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private static final Duration ACTIVATION_KEY_VALIDITY = Duration.ofHours(24);
    private static final Duration PASSWORD_RESET_VALIDITY = Duration.ofMinutes(15);

    private final EmailService emailService;

    /**
     * Envoie un email d'activation de compte utilisateur.
     *
     * @param email l'adresse email du destinataire
     * @param activationKey la clé d'activation à usage unique
     */
    public void sendAccountActivationEmail(String email, String activationKey) {
        String subject = "Activez votre compte";

        String body = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                .container { max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; }
                .header { background-color: #f8f9fa; padding: 20px; text-align: center; }
                .content { padding: 30px; line-height: 1.6; color: #333; }
                .activation-code { 
                    display: inline-block; 
                    padding: 15px 25px; 
                    background-color: #f0f7ff; 
                    border: 2px dashed #4a90e2; 
                    border-radius: 8px; 
                    font-size: 24px; 
                    font-weight: bold; 
                    letter-spacing: 2px; 
                    color: #2c5282; 
                    margin: 20px 0; 
                    text-align: center;
                }
                .footer { 
                    margin-top: 30px; 
                    padding-top: 20px; 
                    border-top: 1px solid #eaeaea; 
                    font-size: 12px; 
                    color: #666; 
                    text-align: center;
                }
                .validity-note { 
                    background-color: #fff8e1; 
                    padding: 10px; 
                    border-radius: 4px; 
                    margin: 15px 0; 
                    font-size: 14px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Bienvenue sur e-tsako</h1>
                </div>
                <div class="content">
                    <h2>Activation de votre compte</h2>
                    <p>Nous vous remercions pour votre inscription.</p>
                    <p>Pour finaliser la création de votre compte, veuillez utiliser le code d'activation suivant :</p>
                    
                    <div class="activation-code">%s</div>
                    
                    <div class="validity-note">
                        ⏱️ <strong>Important :</strong> Ce code est valable pendant %d heures.
                    </div>
                    
                    <p>Si vous n'êtes pas à l'origine de cette demande, vous pouvez ignorer cet email.</p>
                    
                    <p>Cordialement,<br>L'équipe e-tsako</p>
                </div>
                <div class="footer">
                    <p>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>
                    <p>© %d ImRanto. Tous droits réservés.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(activationKey, ACTIVATION_KEY_VALIDITY.toHours(), java.time.Year.now().getValue());

        emailService.envoyerEmail(new EmailRequest(email, subject, body));
    }

    /**
     * Envoie un email de réinitialisation de mot de passe.
     *
     * @param email l'adresse email du destinataire
     * @param resetCode le code de réinitialisation à usage unique
     */
    public void sendPasswordResetEmail(String email, String resetCode) {
        String subject = "Réinitialisation de votre mot de passe";

        String body = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                .container { max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; }
                .header { background-color: #fff5f5; padding: 20px; text-align: center; }
                .content { padding: 30px; line-height: 1.6; color: #333; }
                .reset-code { 
                    display: inline-block; 
                    padding: 15px 25px; 
                    background-color: #fff5f5; 
                    border: 2px dashed #e53e3e; 
                    border-radius: 8px; 
                    font-size: 24px; 
                    font-weight: bold; 
                    letter-spacing: 2px; 
                    color: #c53030; 
                    margin: 20px 0; 
                    text-align: center;
                }
                .security-note { 
                    background-color: #fed7d7; 
                    padding: 12px; 
                    border-radius: 4px; 
                    margin: 20px 0; 
                    font-size: 13px;
                }
                .footer { 
                    margin-top: 30px; 
                    padding-top: 20px; 
                    border-top: 1px solid #eaeaea; 
                    font-size: 12px; 
                    color: #666; 
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Réinitialisation de mot de passe</h1>
                </div>
                <div class="content">
                    <p>Nous avons reçu une demande de réinitialisation du mot de passe associé à votre compte.</p>
                    
                    <p>Veuillez utiliser le code de vérification suivant pour procéder à la réinitialisation :</p>
                    
                    <div class="reset-code">%s</div>
                    
                    <div class="security-note">
                        🔒 <strong>Sécurité :</strong> 
                        <ul>
                            <li>Ce code expire dans %d minutes</li>
                            <li>Ne partagez jamais ce code avec qui que ce soit</li>
                            <li>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer cet email</li>
                        </ul>
                    </div>
                    
                    <p>Pour des raisons de sécurité, ce code est à usage unique et a une durée de validité limitée.</p>
                    
                    <p>Cordialement,<br>L'équipe de sécurité e-tsako</p>
                </div>
                <div class="footer">
                    <p>Cet email a été généré automatiquement. Merci de ne pas y répondre.</p>
                    <p>© %d ImRanto. Tous droits réservés.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(resetCode, PASSWORD_RESET_VALIDITY.toMinutes(), java.time.Year.now().getValue());

        emailService.envoyerEmail(new EmailRequest(email, subject, body));
    }
}