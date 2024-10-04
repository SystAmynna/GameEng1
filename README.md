# Game Engine 2D
**Java avec Swing**
  
## Fonctionnalités:
*✅ Implémenté / ❎ En cours d'implémentation*
- ✅: Présence d'une map 2D (cadrillage de tiles)
- ✅: Présence d'une Camera (avec plusieurs modes: "suivre entité" "freecam"...)
- ✅: Présence d'entités pouvant avoir des propriétés (considéré comme zone déclancheuse, simple entité)
- ✅: Collision par glissement avec les tiles et les entités
- ✅: Fullscreen (F11) (⚠️ BUG SWING: avec une résolution d'écran élevé (dans mon cas 2K) quitter le fullscreen peux faire bugger le redimensionnement de la fenêtre)
- ✅: Menu debug (affiche beaucoup d'infos: coordonnées, tiles, FPS, updates, temps de tâche, entités chargées...)
- ✅: contrôles
- ❎: Gestion des Rooms (changer de map)
- ❎: Trigger zones (utiliser une entité sans rendu)
- ❎: Menu pause (ESC) 
  
## Contrôles
**Z Q S D** ou **➡️⬅️⬆️⬇️** pour se déplacer.  
**ESC** ouvrir le menu.  
**F11** activer / désactiver le plein écran.  
**F3** activer / désactiver le menu débug.  
  
## Notes:
Ce projet est un ancien projet, il ne sera probablement plus mis à jour. 
J'ai rapidement était limité par Swing et j'ai fais face à un bug (de swing) que je n'expliquais pas (le fullscreen faisait lag sur un de mes pc mais pas l'autre, alors que le menu debug me confirmait le maintient de 60fps).
Au moment où j'écris ceci, je suis en train d'apprendre OpenGL (par le biais de LWJGL) afin de monter moi même mon moteur graphique puis mon moteur de jeu, à des fins éducatives.
