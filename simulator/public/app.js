let songs = [];
let currentIndex = -1;
let currentPlaylist = [];
let focusedElement = null;

// Fetch songs
async function loadSongs() {
    try {
        const res = await fetch('/api/songs');
        songs = await res.json();
        renderSongs();
        setupSpatialNavigation();
    } catch (e) {
        document.querySelector('.loading').textContent = "Error loading songs. Make sure rclone is configured.";
    }
}

function renderSongs() {
    const grid = document.getElementById('song-grid');
    grid.innerHTML = '';
    
    if (songs.length === 0) {
        grid.innerHTML = '<div class="no-songs">No music files found in GDrive folder.</div>';
        return;
    }

    songs.forEach((song, idx) => {
        const card = document.createElement('div');
        card.className = 'song-card focusable';
        card.tabIndex = 0;
        card.dataset.index = idx;
        card.innerHTML = `
            <div class="icon">🎵</div>
            <div class="title">${song.title}</div>
            <div class="format">${song.format.toUpperCase()}</div>
        `;
        
        card.addEventListener('click', () => playSong(idx));
        grid.appendChild(card);
    });

    document.querySelector('.loading').style.display = 'none';
}

// Playback Logic
const audio = document.getElementById('audio-player');
const playBtn = document.getElementById('btn-play');
const progressFill = document.getElementById('progress-fill');
const curTimeText = document.getElementById('current-time');
const totalTimeText = document.getElementById('total-time');

function playSong(idx) {
    currentIndex = idx;
    const song = songs[idx];
    
    document.getElementById('player-title').textContent = song.title;
    document.getElementById('player-format').textContent = `Format: ${song.format.toUpperCase()} (GDrive Sync)`;
    
    audio.src = `/api/stream?path=${encodeURIComponent(song.path)}`;
    audio.play();
    playBtn.textContent = '⏸';

    // Auto navigate to now playing screen
    switchScreen('now-playing');
}

playBtn.addEventListener('click', () => {
    if (audio.paused) {
        audio.play();
        playBtn.textContent = '⏸';
    } else {
        audio.pause();
        playBtn.textContent = '▶';
    }
});

document.getElementById('btn-next').addEventListener('click', () => {
    if (currentIndex < songs.length - 1) {
        playSong(currentIndex + 1);
    }
});

document.getElementById('btn-prev').addEventListener('click', () => {
    if (currentIndex > 0) {
        playSong(currentIndex - 1);
    }
});

// Update progress bar
audio.addEventListener('timeupdate', () => {
    if (audio.duration) {
        const progress = (audio.currentTime / audio.duration) * 100;
        progressFill.style.width = `${progress}%`;
        
        curTimeText.textContent = formatTime(audio.currentTime);
        totalTimeText.textContent = formatTime(audio.duration);
    }
});

audio.addEventListener('ended', () => {
    if (currentIndex < songs.length - 1) {
        playSong(currentIndex + 1);
    }
});

function formatTime(secs) {
    const mins = Math.floor(secs / 60);
    const s = Math.floor(secs % 60);
    return `${mins}:${s < 10 ? '0' : ''}${s}`;
}

// Sidebar/Screen navigation
const screens = ['library', 'playlists', 'now-playing'];
function switchScreen(screenName) {
    document.querySelectorAll('.screen').forEach(s => s.classList.remove('active-screen'));
    document.querySelectorAll('.menu-item').forEach(m => m.classList.remove('active'));
    
    document.getElementById(`screen-${screenName}`).classList.add('active-screen');
    const menuItem = document.getElementById(`menu-${screenName.replace('-', '')}`);
    if (menuItem) menuItem.classList.add('active');

    // Refocus inside the active screen
    setTimeout(() => {
        const firstFocusable = document.querySelector(`.active-screen .focusable, #sidebar .active`);
        if (firstFocusable) {
            firstFocusable.focus();
        }
    }, 50);
}

// Spatial Navigation Setup (D-pad Emulator)
function setupSpatialNavigation() {
    // Focus first element
    const first = document.querySelector('.menu-item');
    if (first) first.focus();

    document.addEventListener('keydown', (e) => {
        const active = document.activeElement;
        if (!active || !active.classList.contains('focusable')) return;

        let next = null;
        const focusables = Array.from(document.querySelectorAll('.focusable:not([style*="display: none"])'));
        const activeIndex = focusables.indexOf(active);

        // Simple spatial heuristic mapping
        switch(e.key) {
            case 'ArrowDown':
                // Check if in sidebar
                if (active.classList.contains('menu-item')) {
                    const menuItems = Array.from(document.querySelectorAll('#sidebar .menu-item'));
                    const menuIdx = menuItems.indexOf(active);
                    if (menuIdx < menuItems.length - 1) {
                        next = menuItems[menuIdx + 1];
                    }
                } else {
                    // Navigate grid downwards (heuristic: +4 items or next row)
                    next = focusables[activeIndex + 3] || focusables[focusables.length - 1];
                }
                break;
            case 'ArrowUp':
                if (active.classList.contains('menu-item')) {
                    const menuItems = Array.from(document.querySelectorAll('#sidebar .menu-item'));
                    const menuIdx = menuItems.indexOf(active);
                    if (menuIdx > 0) {
                        next = menuItems[menuIdx - 1];
                    }
                } else {
                    next = focusables[activeIndex - 3] || focusables[0];
                }
                break;
            case 'ArrowRight':
                // Move from sidebar to main content
                if (active.classList.contains('menu-item')) {
                    next = document.querySelector('.active-screen .focusable');
                } else {
                    next = focusables[activeIndex + 1];
                }
                break;
            case 'ArrowLeft':
                // Move from main content back to sidebar
                if (!active.classList.contains('menu-item')) {
                    next = document.querySelector('#sidebar .menu-item.active');
                } else {
                    next = focusables[activeIndex - 1];
                }
                break;
            case 'Enter':
                e.preventDefault();
                active.click();
                if (active.classList.contains('menu-item')) {
                    switchScreen(active.dataset.screen);
                }
                break;
            case ' ':
                e.preventDefault();
                playBtn.click();
                break;
        }

        if (next && focusables.includes(next)) {
            next.focus();
        }
    });
}

// Init
window.onload = () => {
    loadSongs();
    
    // Sidebar clicks
    document.querySelectorAll('.menu-item').forEach(item => {
        item.addEventListener('click', () => switchScreen(item.dataset.screen));
    });
};
