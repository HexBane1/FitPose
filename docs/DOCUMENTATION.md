# FitPose – Documentation technique (étape 1)

## 1. But de l'application

FitPose est une application Android destinée à la musculation. Dans cette première étape, le prototype est un **journal de séries** : l'utilisateur choisit un exercice parmi cinq (Squat, Seated Cable Row, Bench Press, Biceps Curl, Lateral Raises), puis enregistre ses séries avec le poids (en kg), le nombre de répétitions et la date. Chaque série peut être ajoutée, modifiée ou supprimée, et les données sont conservées après la fermeture de l'application.

Ce prototype constitue la première étape d'une application qui, par la suite, analysera la posture de l'utilisateur pendant l'exécution des exercices. Dans ce prototype, le bouton « Analyze form » de l'écran d'un exercice ouvre simplement la caméra du téléphone : l'analyse de la posture elle-même n'est pas encore implémentée et sera disponible dans la version finale de l'application.

## 2. Choix technologiques

- **Android natif** – l'application cible uniquement Android, ce qui donne un accès direct aux API de la plateforme, nécessaire pour les étapes suivantes du projet.
- **Java** – c'est le langage utilisé dans les travaux pratiques du cours, ce qui permet de réutiliser directement les notions apprises.
- **Layouts XML (ConstraintLayout / LinearLayout)** – l'interface est décrite séparément du code ; ConstraintLayout sert aux écrans dont les éléments sont positionnés les uns par rapport aux autres, LinearLayout aux empilements simples.
- **Activities + Intents pour la navigation** – chaque écran est une Activity, et les Intents explicites avec des extras suffisent pour passer d'un écran à l'autre en transmettant le nom de l'exercice ou l'identifiant de la série.
- **Caméra via un Intent** – le bouton « Analyze form » lance l'application caméra du système avec un Intent, sans permission ni dépendance supplémentaire ; l'analyse de la posture à partir de l'image sera ajoutée dans la version finale.
- **Room pour la persistance locale** – Room fournit une couche d'abstraction au-dessus de SQLite, avec des requêtes vérifiées à la compilation et très peu de code à écrire.
- **RecyclerView pour les listes** – il affiche efficacement des listes en recyclant les vues, aussi bien pour les exercices que pour les séries.

## 3. Structure du projet

```
com.mihnea.formcoach
├── ui/
│   ├── MainActivity
│   ├── SetListActivity
│   ├── SetFormActivity
│   ├── ExerciseAdapter
│   └── WorkoutSetAdapter
└── data/
    ├── WorkoutSet
    ├── WorkoutSetDao
    └── AppDatabase
```

### Paquet `ui`

- **MainActivity** – écran d'accueil ; affiche le titre, le sous-titre et la liste des cinq exercices, puis ouvre `SetListActivity` en lui transmettant le nom de l'exercice choisi.
- **SetListActivity** – affiche les séries enregistrées pour un exercice (les plus récentes en premier), un message lorsque la liste est vide, le bouton « Analyze form » qui ouvre la caméra (l'analyse sera ajoutée dans la version finale) et le bouton « Add set » ; la liste est rechargée dans `onResume()`.
- **SetFormActivity** – formulaire d'ajout ou de modification d'une série ; valide les champs, enregistre, met à jour ou supprime la série, puis ferme l'écran.
- **ExerciseAdapter** – adaptateur du RecyclerView qui affiche les noms des exercices et signale le clic sur l'un d'eux.
- **WorkoutSetAdapter** – adaptateur du RecyclerView qui affiche le poids, les répétitions et la date de chaque série et signale le clic sur une ligne.

### Paquet `data`

- **WorkoutSet** – entité Room (table `workout_sets`) représentant une série : identifiant, exercice, poids, répétitions et date.
- **WorkoutSetDao** – interface DAO définissant l'insertion, la mise à jour, la suppression et les deux requêtes de lecture (par exercice et par identifiant).
- **AppDatabase** – base de données Room, accessible par un singleton via `getDatabase(Context)`.

### Fichiers de ressources

- `res/layout/activity_main.xml`, `activity_set_list.xml`, `activity_set_form.xml` – les layouts des trois écrans.
- `res/layout/item_exercise.xml`, `item_workout_set.xml` – les layouts d'une ligne de chaque liste.
- `res/values/strings.xml` – tous les textes de l'interface, ainsi que le tableau `exercises` contenant les noms des exercices.
- `res/values/colors.xml` – toutes les couleurs de l'application.
- `res/values/themes.xml` – le thème clair de l'application (sans mode sombre) et les styles partagés par les écrans.
- `AndroidManifest.xml` – déclaration des trois Activities, toutes en mode portrait.
