// Charger l'API YouTube uniquement si elle n'est pas déjà chargée
if (typeof YT === 'undefined' || typeof YT.Player === 'undefined') {
    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    tag.onload = function() {
        console.log("Script API YouTube chargé.");
};
    tag.onerror = function() {
    console.error("Erreur lors du chargement du script API YouTube.");
};
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
} else {
    onYouTubeIframeAPIReady();
}



    // extraction identifiant video Youtube
    function extractVideoId(url) {
        if (url != null) {
            var start = "https://youtu.be/";
            var starts = "https://www.youtube.com/embed/";

            if (url.startsWith("https://youtu.be/")) {
                return url.substring(start.length);
            }

            if (url.startsWith("https://www.youtube.com/watch?v=")) {
                var videoId = url.split('v=')[1];
                if (videoId.indexOf('&') !== -1) {
                    videoId = videoId.split('&')[0];
                }
                return videoId;
            }

            if (url.startsWith("https://www.youtube.com/embed/")) {
                return url.substring(starts.length);
            }
        }

        return null;
    }


    function consoleView(value) {
        if (value === 25) {
            console.log("25%");
        } else if (value === 50) {
            console.log("50%");
        } else if (value === 75) {
            console.log("75%");
        } else if (value === 100) {
            console.log("100%");
        } else {
            console.log("tsy niditra");
            console.log("value =" + value);
        }
    }
       

        // Fonction appelée par l'API YouTube lorsqu'elle est prête
        function onYouTubeIframeAPIReady() {
            console.log("API YouTube est prête");
        }



        // Fonction appelée lorsque le lecteur est prêt
        function onPlayerReady(event) {
            var playButton = $('#playButton');
            console.log("Le lecteur est prêt");

            document.getElementById('progress-bar').addEventListener('input', seekVideo);

            updatePlayButtonText();
            updateDuration();

            playButton.on('click', function() {
                if (player && player.getPlayerState() === YT.PlayerState.PLAYING) {
                    player.pauseVideo(); 
                } else if (player) {
                    player.playVideo(); 
                }

                updatePlayButtonText();
            });

            player.addEventListener('onStateChange', function(event) {
                updatePlayButtonText();
            });

            function updatePlayButtonText() {
                if (player && player.getPlayerState() === YT.PlayerState.PLAYING) {
                    playButton.text('Pause'); 
                } else {
                    playButton.text('Play'); 
                }
            }
        }

        function onPlayerStateChange(event) {
            if (event.data === YT.PlayerState.PLAYING) {
                updateTime();
            }
        }


        function updateTime() {
            const currentTime = player.getCurrentTime();
            const duration = player.getDuration();
            document.getElementById('current-time').textContent = formatTime(currentTime);
            document.getElementById('duration').textContent = formatTime(duration);

            // Mettre à jour la barre de progression
            document.getElementById('progress-bar').value = (currentTime / duration) * 100;
            var value = (currentTime / duration) * 100;
            consoleView(value,duration);


            if (player.getPlayerState() === YT.PlayerState.PLAYING) {
                setTimeout(updateTime, 1000);
            }



        }


        function seekVideo(event) {
            const value = event.target.value;
            const duration = player.getDuration();
            player.seekTo(duration * (value / 100));
        }


        function updateDuration() {
            const duration = player.getDuration();
            document.getElementById('duration').textContent = formatTime(duration);
        }


        function formatTime(seconds) {
            const minutes = Math.floor(seconds / 60);
            const secs = Math.floor(seconds % 60);
            return `${pad(minutes)}:${pad(secs)}`;
        }

        function pad(number) {
            return number < 10 ? '0' + number : number;
        }


         // Variable globale pour le lecteur
         var player;
         var playerReadyDeferred = $.Deferred();

        // Écouteur pour l'événement d'ouverture de la modal
        $('#exampleModalCenter').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);

            var videoUrl = button.data('url_video'); 
            var campaign_type = button.data('type_campaign');

            // if type de campaign = Publication :
            console.log(campaign_type);

            
            var playerDiv = document.getElementById('player');
            playerDiv.innerHTML = ''; // Nettoyer tout contenu précédent
            
            if (campaign_type == "Publication") {

                // type campaign = publication

                if (typeof player !== 'undefined') {
                    player.destroy(); // Détruire le lecteur YouTube existant
                    player = undefined; // Réinitialiser la variable player
                }
                

                var videoElement = document.createElement('video');
                videoElement.setAttribute('controls', ''); // Ajoute les contrôles de lecture
                videoElement.setAttribute('id', 'video'); 
        
                // Définir la source de la vidéo
                var sourceElement = document.createElement('source');
                sourceElement.src = videoUrl; // Chemin vers la vidéo sur le serveur
                sourceElement.type = 'video/mp4'; // Type de la vidéo
        
                // Ajouter la source à l'élément vidéo
                videoElement.appendChild(sourceElement);
        
                // Insérer l'élément vidéo dans le div avec l'id "player"
                var playerDiv = document.getElementById('player');
                playerDiv.innerHTML = ''; // Nettoyer tout contenu précédent
                playerDiv.appendChild(videoElement);


                // CONTROLLE VIDEO 

                // Récupérer l'élément vidéo et la barre de progression
                var video = document.getElementById('video');
                var progressBar = document.getElementById('progress-bar');

                // Mettre à jour la barre de progression lorsque la vidéo se joue
                video.addEventListener('timeupdate', function() {
                    var value = (100 / video.duration) * video.currentTime;
                    var duration = video.duration;
                    consoleView(value,duration)
                    progressBar.value = value;
                });


                // Mettre à jour la vidéo lorsque l'utilisateur fait glisser la barre de progression
                progressBar.addEventListener('input', function() {
                    var time = (progressBar.value / 100) * video.duration;
                    video.currentTime = time;
                });

                // Contrôles Play/Pause
                var playPauseButton = document.getElementById('playButton');

                // Ajoute un écouteur d'événements pour le bouton
                playPauseButton.addEventListener('click', function() {
                    if (video.paused) {
                        video.play();
                        playPauseButton.textContent = 'Pause';
                    } else {
                        video.pause();
                        playPauseButton.textContent = 'Play';
                    }
                });
                
            } else if (campaign_type == "Youtube"){

                //  type de campaign = Youtube :

                var video_Id = extractVideoId(videoUrl);

                console.log(player);
                console.log("mandalo else");


                // if (!player) {
                    player = new YT.Player('player', {
                        height: '100%',
                        width: '100%',  
                        videoId: video_Id, 
                        playerVars: {
                            'controls': 0, // Désactive la barre de contrôle (lecture, pause, etc.)
                            'disablekb': 1, // Désactive les raccourcis clavier
                            'modestbranding': 1, // Réduit l'image de marque YouTube
                            'rel': 0, // Ne montre pas les vidéos suggérées à la fin
                            'showinfo': 0, // Masque les informations sur la vidéo (titre, uploader)
                            'fs': 0, // Désactive le bouton plein écran
                            'iv_load_policy': 3, // Désactive les annotations
                            'autohide': 1, // Masque les commandes après la lecture de la vidéo
                            'playsinline': 1, // Joue la vidéo en ligne au lieu de plein écran sur mobile
                            'enablejsapi': 1 // Active l'API JavaScript
                        },
                        events: {
                            'onReady': onPlayerReady,
                            'onError': function(event) {
                                console.error("Erreur dans le lecteur:", event.data);
                            },
                            'onStateChange': onPlayerStateChange
                        }
                    });
                // } else {
                //     player.loadVideoById(video_Id); // Change la vidéo et commence la lecture // youtube makany amn youtube
                // }


                playerReadyDeferred.done(function() {
                    if (player && typeof player.cueVideoById === 'function') {
                        player.cueVideoById(video_Id);
                        console.log("Vidéo chargée avec ID:", video_Id);
                    } else {
                        console.error('La méthode cueVideoById n\'est pas disponible.');
                    }
                }).fail(function() {
                    console.error("Le lecteur n'est pas prêt.");
                });



            } else {

                // CAROUSEL 
                console.log("mandalo carousel");
              
                // ETO NO MAKA NY SARY CAROUSEL
                var carouselImages = button.data('carousel_images'); 
                
                // Trouver le conteneur pour le carrousel dans la modal
                const carouselContainer = document.getElementById('player');
                const footerModal = document.getElementById('modal-footer');
                
                // Nettoyer le conteneur
                carouselContainer.innerHTML = '';
                
                // Créer le carrousel et ajouter les images
                const carouselDiv = document.createElement('div');
                carouselDiv.id = 'myCarousel';
                carouselDiv.className = 'carousel slide';
                carouselDiv.setAttribute('data-ride', 'carousel');
                
                // Création des indicateurs
                const indicators = document.createElement('ol');
                indicators.className = 'carousel-indicators';
                carouselImages.forEach((_, index) => {
                    const li = document.createElement('li');
                    li.setAttribute('data-target', '#myCarousel');
                    li.setAttribute('data-slide-to', index);
                    if (index === 0) li.className = 'active'; // Première diapositive active par défaut
                    indicators.appendChild(li);
                });

                // Création de l'enveloppe des diapositives
                const innerDiv = document.createElement('div');
                innerDiv.className = 'carousel-inner';
                
                // Ajout des diapositives
                // ETO NO MAPIDITRA NY SARY CAROUSEL

                console.log(carouselImages);

                carouselImages.forEach((image, index) => {
                    const itemDiv = document.createElement('div');
                    itemDiv.className = index === 0 ? 'item active' : 'item';
                    
                    const img = document.createElement('img');
                    img.src = image.urlImage;
                    img.className = 'd-block w-100';
                    
                    itemDiv.appendChild(img);
                    innerDiv.appendChild(itemDiv);
                });

                // Création des contrôles gauche et droite
                const leftControl = document.createElement('a');
                leftControl.className = 'left carousel-control';
                leftControl.href = '#myCarousel';
                leftControl.setAttribute('data-slide', 'prev');
                leftControl.innerHTML = '<span class="glyphicon glyphicon-chevron-left"></span><span class="sr-only">Précédent</span>';
                
                const rightControl = document.createElement('a');
                rightControl.className = 'right carousel-control';
                rightControl.href = '#myCarousel';
                rightControl.setAttribute('data-slide', 'next');
                rightControl.innerHTML = '<span class="glyphicon glyphicon-chevron-right"></span><span class="sr-only">Suivant</span>';
                
                // Assemblage des éléments dans le carrousel
                carouselDiv.appendChild(indicators);
                carouselDiv.appendChild(innerDiv);


                carouselDiv.appendChild(leftControl);
                carouselDiv.appendChild(rightControl);
                
                
                // Ajout du carrousel au conteneur de la page
                carouselContainer.appendChild(carouselDiv);

               // Activer le carrousel Bootstrap après l'ajout
                $('#myCarousel').carousel({
                    interval: 3000, // Diapositive toutes les 3 secondes
                    ride: 'carousel',
                    pause: false
                });

                
                // Forcer le démarrage du cycle
                $('#myCarousel').carousel('cycle');
                
                console.log("Carousel initialized");

            }


        });

        // Écouteur pour l'événement de fermeture de la modal
        $('#exampleModalCenter').on('hidden.bs.modal', function () {
           // Arrêter la vidéo YouTube si le lecteur existe
            if (player && typeof player.stopVideo === 'function') {
                player.stopVideo(); 
                player.destroy(); // Détruire le lecteur YouTube
                player = undefined; // Réinitialiser la variable player
            }

            // Arrêter la vidéo HTML5 si elle existe
            // var videoElement = document.querySelector('#player');
            var videoElement = document.getElementById('video');
            if (videoElement) {
                videoElement.pause();
                videoElement.src = '';
            }

            // reinitialiser
            var carousel = document.getElementById('myCarousel');
            if (carousel){

            }
            
        });

