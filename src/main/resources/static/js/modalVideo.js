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

        // reference pour le lecteur video 
        let youtubeMilestones = {
            "25%": false,
            "50%": false,
            "75%": false,
            "100%": false
        };

        function checkVideoMilestones(currentTime, duration, milestones) {
            const tolerance = 0.5; // Tolérance de 0.5 seconde pour considérer que la vidéo est à la fin
            // Vérifier les étapes de progression
            if (!milestones["25%"] && currentTime >= duration * 0.25) {
                console.log("La vidéo est à 25%");
                milestones["25%"] = true;
                quart_lecture = 1;
                transmettreEvent();
                quart_lecture = 0;
            }
            if (!milestones["50%"] && currentTime >= duration * 0.50) {
                console.log("La vidéo est à 50%");
                milestones["50%"] = true;
                demi_lecture = 1;
                transmettreEvent();
                demi_lecture = 0;
            }
            if (!milestones["75%"] && currentTime >= duration * 0.75) {
                console.log("La vidéo est à 75%");
                milestones["75%"] = true;
                troisquart_lecture = 1;
                transmettreEvent();
                troisquart_lecture = 0;
            }
            if (!milestones["100%"] && (duration - currentTime <= tolerance)) {
                console.log("La vidéo est à 100%");
                milestones["100%"] = true;
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

        function onPlayerStateChange(event, logo_end) {
            if (event.data === YT.PlayerState.PLAYING) {
                updateTime();
            }
            if (event.data === YT.PlayerState.ENDED) {
                updateTime();
                fin_lecture = 1;
                transmettreEvent();
                fin_lecture = 0;
                logoFin(logo_end); // Afficher le logo de fin
            }
        }

        

        function updateTime() {
            const currentTime = player.getCurrentTime();
            const duration = player.getDuration();
            document.getElementById('current-time').textContent = formatTime(currentTime);
            document.getElementById('duration').textContent = formatTime(duration);

            // Mettre à jour la barre de progression
            document.getElementById('progress-bar').value = (currentTime / duration) * 100;


            checkVideoMilestones(currentTime, duration, youtubeMilestones);


            if (player.getPlayerState() === YT.PlayerState.PLAYING) {
                setTimeout(updateTime, 500);
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

        

        function logoFin(logo_end) {
            var startLogoDiv = document.createElement('div');
            startLogoDiv.id = 'end-logo'; // Ajouter l'ID
            startLogoDiv.style.display = 'block'; // Ajouter du style
            startLogoDiv.style.cursor = 'pointer'; // Ajouter du style

            // Créer l'image à l'intérieur du div start-logo
            var startLogoImg = document.createElement('img');
            startLogoImg.alt = 'Logo fin'; // Ajouter l'attribut alt
            startLogoImg.src = logo_end; // Vous pouvez ajouter une source ici ou plus tard

            // Ajouter l'image au div
            startLogoDiv.appendChild(startLogoImg);

            // startLogoImg.addEventListener('click', function() {
            //     affichage(button, campaign_type, videoUrl);
            // });

            if (player && typeof player.stopVideo === 'function') {
                player.stopVideo(); 
                player.destroy(); // Détruire le lecteur YouTube
                player = undefined; // Réinitialiser la variable player
                resetMilestones();
            }

            // Arrêter la vidéo HTML5 si elle existe
            // var videoElement = document.querySelector('#player');
            var videoElement = document.getElementById('video');
            if (videoElement) {
                videoElement.pause();
                videoElement.src = '';
            }

            
            var playerDiv = document.getElementById('player');
            playerDiv.innerHTML = ''; // Nettoyer tout contenu précédent
            playerDiv.appendChild(startLogoDiv); //ajout logo debut video 
        }


        // Variable globale pour le lecteur
        var player;
        var playerReadyDeferred = $.Deferred();


    var campaignId;
    var campaignVideoId; 


    // // initialisation des parametres 
    var impression = 0;
    var lancement = 0;
    var vue = 0;
    var skip_video = 0;
    var quart_lecture = 0;
    var demi_lecture = 0;
    var troisquart_lecture = 0;
    var fin_lecture = 0;



    // fonction de transaction
    function transmettreEvent() {
        const url = '/api/trans/save';
        
        fetch(url, {
            method: 'POST',
            body: JSON.stringify({
                date_trans: new Date(),
                id_campaign: campaignId,
                id_campaign_video: campaignVideoId,
                impression: impression,
                lancement: lancement,
                vue: vue,
                skip_video: skip_video,
                quart_lecture: quart_lecture,
                demi_lecture: demi_lecture,
                troisquart_lecture: troisquart_lecture,
                fin_lecture: fin_lecture
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            console.log(response); // Ajout du log pour voir la réponse
            return response.json(); // Tentative de conversion en JSON
        })
        .then(data => console.log('Événements enregistrés avec succès:', data))
        .catch(error => console.error('Erreur lors de l\'enregistrement des événements:', error));
    }

        // Écouteur pour l'événement d'ouverture de la modal
        $('#exampleModalCenter').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget);

            var videoUrl = button.data('url_video'); 
            var campaign_type = button.data('type_campaign');
            var logo_begin = button.data('logo_begin');
            var logo_end = button.data('logo_end');

            campaignId = button.data('campaign_id');
            campaignVideoId = button.data('campaign_video_id');

            console.log(campaignId);
            console.log(button.data('campaign_id'));

            // changement etat impression STAT
            impression = 1
            // transmettre donnée
            transmettreEvent();
            // reinitialiser l'impression
            impression = 0;


            // affichage premier logo
            var startLogoDiv = document.createElement('div');
            startLogoDiv.id = 'start-logo'; 
            startLogoDiv.style.display = 'block'; 
            startLogoDiv.style.cursor = 'pointer'; 

            var startLogoImg = document.createElement('img');
            startLogoImg.alt = 'Logo début'; 
            startLogoImg.src = logo_begin; 

            startLogoDiv.appendChild(startLogoImg);

            startLogoImg.addEventListener('click', function() {
                // Appeler la fonction affichage quand le logo est cliqué
                affichage(button, campaign_type, videoUrl, logo_end);
                lancement = 1;
                vue = 1;
                transmettreEvent();
                lancement = 0;
                vue = 0;
            });
    
            var playerDiv = document.getElementById('player');
            playerDiv.innerHTML = ''; // Nettoyer tout contenu précédent
            playerDiv.appendChild(startLogoDiv); //ajout logo debut video 

        });




        function affichage(button, campaign_type, videoUrl, logo_end) {
             // mila atao anaty fonction
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

                // references lecture video 
                let localMilestones = {
                    "25%": false,
                    "50%": false,
                    "75%": false,
                    "100%": false
                };

                // Récupérer l'élément vidéo et la barre de progression
                var video = document.getElementById('video');
                var progressBar = document.getElementById('progress-bar');

                // Mettre à jour la barre de progression lorsque la vidéo se joue
                video.addEventListener('timeupdate', function() {
                    var value = (100 / video.duration) * video.currentTime;
                    progressBar.value = value;
                   
                    var currentTime = video.currentTime;
                    var duration = video.duration; 
                    checkVideoMilestones(currentTime, duration, localMilestones);
                
                });

                video.addEventListener('ended', function() {
                    console.log("La vidéo est terminée !");
                    logoFin(logo_end);
                    fin_lecture = 1;
                    transmettreEvent();
                    fin_lecture = 0;
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

                // extract l'id du video à partir de l'url video 
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
                            'enablejsapi': 1, // Active l'API JavaScript
                            'color': 'white',
                            'theme': 'dark'
                        },
                        events: {
                            'onReady': onPlayerReady,
                            'onError': function(event) {
                                console.error("Erreur dans le lecteur:", event.data);
                            },
                            'onStateChange': function(event) {
                                onPlayerStateChange(event, logo_end);
                            }
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
                // const footerModal = document.getElementById('modal-footer');
                
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
        }



        function resetMilestones() {
            youtubeMilestones = {
                "25%": false,
                "50%": false,
                "75%": false,
                "100%": false
            };
        }

        // Écouteur pour l'événement de fermeture de la modal
        $('#exampleModalCenter').on('hidden.bs.modal', function () {


            campaignId = null;
            campaignVideoId = null;

           // Arrêter la vidéo YouTube si le lecteur existe
            if (player && typeof player.stopVideo === 'function') {
                player.stopVideo(); 
                player.destroy(); // Détruire le lecteur YouTube
                player = undefined; // Réinitialiser la variable player
                resetMilestones();
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



   