// let status = true;


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
            console.log("Le lecteur est prêt");
            event.target.playVideo(); 
            bouton_skip();
            document.getElementById('progress-bar').addEventListener('input', seekVideo);

            updateDuration();
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

            if (player && typeof player.stopVideo === 'function') {
                player.stopVideo(); 
                player.destroy(); // Détruire le lecteur YouTube
                player = undefined; // Réinitialiser la variable player
                resetMilestones();
            }

            // Arrêter la vidéo HTML5 si elle existe
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



        function bouton_skip() {
            setTimeout(function () {
                var bouton_skip = document.getElementById('bouton_skip');
                var button = document.createElement('button');
                button.type = 'button';
                button.innerHTML = 'Skip Vidéo';
                button.className = 'btn btn-primary';
                
                bouton_skip.appendChild(button);

                var videoElement = document.getElementById('video');
                button.addEventListener('click', function() {
                    if (videoElement) {
                        var endedEvent = new Event('ended');
                        video.dispatchEvent(endedEvent); 
                        // video.currentTime = video.duration; 
                        skip_video = 1;
                        transmettreEvent();
                        skip_video = 0;
                        bouton_skip.innerHTML = '';
                    } else if (player) {
                        player.seekTo(player.getDuration()); 
                        skip_video = 1;
                        transmettreEvent();
                        skip_video = 0;
                        bouton_skip.innerHTML = '';
                    }
                });


            }, 3000);
        }


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
                video.play();
                bouton_skip();

                var progressBar = document.getElementById('progress-bar');

                // Mettre à jour la barre de progression lorsque la vidéo se joue
                video.addEventListener('timeupdate', function() {
                    var value = (100 / video.duration) * video.currentTime;
                    progressBar.value = value;
     
                    var currentTime = video.currentTime;
                    var duration = video.duration; 
                    checkVideoMilestones(currentTime, duration, localMilestones);

                    document.getElementById('current-time').textContent = formatTime(currentTime);
                    document.getElementById('duration').textContent = formatTime(duration);


                
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

                // // Contrôles Play/Pause
                // var playPauseButton = document.getElementById('playButton');

                // // Ajoute un écouteur d'événements pour le bouton
                // playPauseButton.addEventListener('click', function() {
                //     if (video.paused) {
                //         video.play();
                //         playPauseButton.textContent = 'Pause';
                //     } else {
                //         video.pause();
                //         playPauseButton.textContent = 'Play';
                //     }
                // });
                
            } else if (campaign_type == "Youtube"){

                //  type de campaign = Youtube :

                // extract l'id du video à partir de l'url video 
                var video_Id = extractVideoId(videoUrl);

                console.log(player);
                console.log("type youtube");



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
                                console.error("Erreur dans le lecteur ou url qui n'existe pas:", event.data);
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
                
                // Nettoyer le conteneur
                carouselContainer.innerHTML = '';
                
                // Créer le carrousel et ajouter les images
                const carouselDiv = document.createElement('div');
                carouselDiv.id = 'my_carousel';
                carouselDiv.className = 'carousel slide';
                // carouselDiv.setAttribute('data-ride', 'carousel');

                
                // Création des indicateurs
                const indicators = document.createElement('ol');
                indicators.className = 'carousel-indicators';
                carouselImages.forEach((_, index) => {
                    const li = document.createElement('li');
                    li.setAttribute('data-target', '#my_carousel');
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
                leftControl.href = '#my_carousel';
                leftControl.setAttribute('data-slide', 'prev');
                leftControl.innerHTML = '<span class="glyphicon glyphicon-chevron-left"></span><span class="sr-only">Précédent</span>';
                
                const rightControl = document.createElement('a');
                rightControl.className = 'right carousel-control';
                rightControl.href = '#my_carousel';
                rightControl.setAttribute('data-slide', 'next');
                rightControl.innerHTML = '<span class="glyphicon glyphicon-chevron-right"></span><span class="sr-only">Suivant</span>';
                
                // Assemblage des éléments dans le carrousel
                carouselDiv.appendChild(indicators);
                carouselDiv.appendChild(innerDiv);


                carouselDiv.appendChild(leftControl);
                carouselDiv.appendChild(rightControl);

                console.log("nombre de carrousel : " + $('#my_carousel').length);

                // Supprimer tout carrousel déjà présent pour éviter des conflits
                if ($('#my_carousel').length) {
                    $('#my_carousel').carousel('dispose'); 
                }

                let totalImages = carouselImages.length;
                // Ajout du carrousel au conteneur de la page
                carouselContainer.appendChild(carouselDiv);

                // supprimer le controlle des videos
                // var sup_temps = document.getElementById('temps_vid');
                // sup_temps.innerHTML = ''; 

                console.log("nombre de carrousel : " + $('#my_carousel').length);


                if ($('#my_carousel').length) {
                    $('#my_carousel').carousel({
                        interval: 5000,  
                        pause: false     
                    });
                    $('#my_carousel').carousel('cycle');
                }

                console.log("Total image carrousel : " + totalImages);


                if ($('#my_carousel').length) {
                    $('#my_carousel').on('slid.bs.carousel', function (event) {
                        currentSlide = $(event.relatedTarget).index();
                        console.log('Diapositive actuelle:', currentSlide);
                        
                        checkCycleStage(totalImages);
            

                        if (currentSlide === totalImages - 1) {
                            
                            
                            setTimeout(function(){$('#my_carousel').carousel({
                                interval: false
                            });}, 5000);

                            $('#my_carousel').remove();

                            setTimeout(logoFin(logo_end),5000);
                            

                        }
                    });
                } else {
                    console.log('Carrousel non trouvé dans le DOM.');
                }

                // Ajouter des logs détaillés pour traquer les événements
                setTimeout(function() {
                    console.log('Vérification des événements après suppression.');
                    var events = $._data($('#my_carousel')[0], 'events');
                    if (events) {
                        console.log('Événements encore présents :', events);
                    } else {
                        console.log('Aucun événement restant.');
                    }
                }, 500);


            }
        }



                // Initialiser les variables pour suivre l'état du carrousel
                // let totalImages = document.querySelectorAll('#my_carousel .carousel-inner .item').length;
                let currentSlide = 0; // On commence à la première diapositive
                let quarterReached = false;
                let halfReached = false;
                let threeQuarterReached = false;
                let endReached = false;

                // Fonction pour vérifier les étapes du cycle
                function checkCycleStage(totalImages) {
                    let quarterSlide = Math.round(totalImages / 4);
                    let halfSlide = Math.round(totalImages / 2);
                    let threeQuarterSlide = Math.round((3 * totalImages) / 4);
                    let endSlide = totalImages - 1; // Index de la dernière image

                    // Un quart du cycle atteint
                    if (currentSlide === quarterSlide && !quarterReached) {
                        quarterReached = true;
                        console.log('Un quart du cycle atteint');
                        quart_lecture = 1; // Activer la variable `quart_lecture`
                        transmettreEvent(); // Transmettre l'événement
                        quart_lecture = 0;
                    }

                    // La moitié du cycle atteinte
                    if (currentSlide === halfSlide && !halfReached) {
                        halfReached = true;
                        console.log('La moitié du cycle atteinte');
                        demi_lecture = 1; // Activer la variable `demi_lecture`
                        transmettreEvent();
                        demi_lecture = 0;
                    }

                    // Les trois quarts du cycle atteints
                    if (currentSlide === threeQuarterSlide && !threeQuarterReached) {
                        threeQuarterReached = true;
                        console.log('Les trois quarts du cycle atteints');
                        troisquart_lecture = 1; // Activer la variable `troisquart_lecture`
                        transmettreEvent();
                        troisquart_lecture = 0;
                    }

                    // Fin du cycle atteinte
                    if (currentSlide === endSlide && !endReached) {
                        endReached = true;
                        console.log('Fin du cycle atteinte');
                        fin_lecture = 1; // Activer la variable `fin_lecture`
                        transmettreEvent();
                        fin_lecture = 0;
                    }

                    quarterReached = false;
                    halfReached = false;
                    threeQuarterReached = false;
                    endReached = false;

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


            // Vérifie si le carrousel existe avant de procéder
            if ($('#my_carousel').length) {
                
                // Supprimer l'écouteur d'événement avant de désactiver le carrousel
                $('#my_carousel').off('slid.bs.carousel');
                
                // Désactiver le carrousel avec Bootstrap
                $('#my_carousel').carousel('dispose'); 

                console.log('Carrousel arrêté et écouteur supprimé');
            }

            const carouselContainer = document.getElementById('player');
            carouselContainer.innerHTML = '';

            var bouton_skip = document.getElementById('bouton_skip');
            bouton_skip.innerHTML = '';

            
        });




        // PROGRESS BAR



        document.addEventListener("DOMContentLoaded", function() {
            
            const progressBars = document.querySelectorAll(".progress-bar");

            progressBars.forEach(progressBar => {
                
                const startDateText = progressBar.getAttribute("data-start");
                const endDateText = progressBar.getAttribute("data-end");

                const currentDate = new Date();
                const startDate = parseDate(startDateText);
                const endDate = parseDate(endDateText);
        
                // Calculer la progression
                const totalDuration = endDate - startDate;
                const currentDuration = currentDate - startDate;
                const progressPercent = (currentDuration / totalDuration) * 100;
        
                // if (progressPercent >= 0 && progressPercent <= 100) {
                //     progressBar.style.width = progressPercent + "%";
                //     progressBar.innerHTML = Math.floor(progressPercent) + "%";
                //     // progressBar.setAttribute("aria-valuenow", progressPercent);
                // } else if (progressPercent > 100) {
                //     progressBar.style.width = "100%";
                //     progressBar.innerHTML = "100%";
                // } else {
                //     progressBar.style.width = "0%";
                //     progressBar.innerHTML = "0%";
                // }

                const indicator = progressBar.querySelector(".progress-indicator");
                if (progressPercent >= 0 && progressPercent <= 100) {
                    indicator.style.left = progressPercent + "%"; // Ajuster la position de l'indicateur
                    indicator.style.height = "100%"; // S'assurer qu'il occupe toute la hauteur
                } else if (progressPercent > 100) {
                    indicator.style.left = "100%";
                } else {
                    indicator.style.left = "0%";
                }

            });
        });



        // Fonction pour convertir 'dd/MM/yyyy HH:mm' en Date
        function parseDate(dateString) {
            const [day, month, yearAndTime] = dateString.split('/');
            const [year, time] = yearAndTime.split(' ');
            const [hours, minutes] = time.split(':');
            
            // Les mois en JavaScript sont indexés de 0 (janvier) à 11 (décembre)
            return new Date(year, month - 1, day, hours, minutes);
        }


        // STATISTIQUE CAMPAIGN


        // Fonction pour charger et afficher la modale
        function openCampaignModal(id_campaign) {

            console.log("Fonction openCampaignModal appelée avec id_campaign:", id_campaign);

            // Effectue une requête AJAX pour récupérer le fragment de la modale
            fetch(`/statistique_campaign?id_campaign=${id_campaign}`)
                .then(response => {
                    console.log("Réponse reçue :", response);
                    return response.text();
                })
                .then(html => {
                    console.log("HTML reçu :", html);

                    // Injecte le fragment de la modale dans la page
                    document.getElementById('modalPlaceholder').innerHTML = html;

                    // Affiche la modale après avoir chargé son contenu
                    $('#statistique_modal').modal('show');
                })
                .catch(error => console.error('Erreur lors du chargement des campagnes:', error));

        }


        // STATUS CAMPAIGN

        document.addEventListener('DOMContentLoaded', function () {
            document.querySelectorAll('.campaign-status-checkbox').forEach(function (checkbox) {
                checkbox.addEventListener('change', function () {
                    const campaignId = this.getAttribute('data-campaign-id');
                    const newStatus = this.checked ? 1 : 0;
        
                    // Envoi de la requête AJAX
                    fetch(`/api/campaign/updateStatus`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content') // CSRF token
                        },
                        body: JSON.stringify({
                            id: campaignId,
                            status: newStatus
                        })
                    }).then(response => {
                        if (response.ok) {
                            console.log('Status updated successfully');
                        } else {
                            console.error('Failed to update status');
                        }
                    }).catch(error => {
                        console.error('Error:', error);
                    });
                });
            });
        });