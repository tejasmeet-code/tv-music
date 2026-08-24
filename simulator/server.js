const express = require('express');
const { exec, spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = 3000;
const FOLDER_ID = '1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd';

app.use(express.static(path.join(__dirname, 'public')));
app.use(express.json());

// API to list all files recursively from GDrive folder using local rclone
app.get('/api/songs', (req, res) => {
    const cmd = `rclone lsf gdrive: --drive-root-folder-id ${FOLDER_ID} --drive-shared-with-me -R`;
    exec(cmd, (err, stdout, stderr) => {
        if (err) {
            console.error("Rclone error:", err);
            return res.status(500).json({ error: "Failed to list files from GDrive" });
        }
        
        const files = stdout.split('\n')
            .map(line => line.trim())
            .filter(line => line.length > 0 && !line.endsWith('/')) // Filter out folders
            .filter(file => {
                // Filter audio and video files
                const ext = path.extname(file).toLowerCase();
                return ['.mp3', '.m4a', '.wav', '.aac', '.flac', '.ogg', '.mkv', '.mp4', '.webm'].includes(ext);
            })
            .map((file, index) => {
                return {
                    id: `song_${index}`,
                    title: path.basename(file, path.extname(file)),
                    path: file,
                    fileName: path.basename(file),
                    format: path.extname(file).substring(1)
                };
            });
            
        res.json(files);
    });
});

// API to stream a song from GDrive using rclone cat
app.get('/api/stream', (req, res) => {
    const filePath = req.query.path;
    if (!filePath) {
        return res.status(400).send("Path parameter required");
    }

    console.log(`Streaming: ${filePath}`);
    res.setHeader('Content-Type', 'audio/mpeg');

    // Run rclone cat to stream the file directly
    const rclone = spawn('rclone', [
        'cat',
        `gdrive:${filePath}`,
        '--drive-root-folder-id', FOLDER_ID,
        '--drive-shared-with-me'
    ]);

    rclone.stdout.pipe(res);

    rclone.stderr.on('data', (data) => {
        console.error(`rclone stderr: ${data}`);
    });

    req.on('close', () => {
        rclone.kill();
    });
});

app.listen(PORT, () => {
    console.log(`GDrive Music TV Simulator running at http://localhost:${PORT}`);
});
