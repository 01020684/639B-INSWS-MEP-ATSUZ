-- Database schema

-- !Ups

CREATE TABLE artists(
    artist_id       UUID,
    name            VARCHAR(255),
    PRIMARY KEY(artist_id)
);

CREATE TABLE albums(
    album_id        UUID,
    artist_id       UUID,
    release_year    INT,
    title           VARCHAR(255),
    tracks_no       INT,
    PRIMARY KEY(album_id)
);

CREATE TABLE tracks(
    track_id        UUID,
    album_id        UUID,
    track_no        INT,
    title           VARCHAR(255),
    duration        INT,
    PRIMARY KEY(track_id)
);

INSERT INTO artists (artist_id, name) VALUES('40e6215d-b5c6-4896-987c-f30f3678f608', 'Michael Jackson');
INSERT INTO artists (artist_id, name) VALUES('b9428932-489b-44ae-95b6-99a1af7b46dd', 'Pink Floyd');
INSERT INTO artists (artist_id, name) VALUES('4368929f-4ea4-4685-b778-c7e7fc83ea05', 'AC/DC');
INSERT INTO artists (artist_id, name) VALUES('6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 'Eminem');
INSERT INTO artists (artist_id, name) VALUES('101ee1b3-9da2-4590-a942-291c80e6f376', 'Whitney Houston');
INSERT INTO artists (artist_id, name) VALUES('652a329e-fabe-4c99-bf23-28dd08d2f507', 'Fleetwood Mac');

INSERT INTO albums (album_id, artist_id, release_year, title, tracks_no)
    VALUES ('6d4f377c-c10f-4d4f-b4ae-96776f0270e1', '40e6215d-b5c6-4896-987c-f30f3678f608', 1982, 'Thriller', 9);
INSERT INTO albums (album_id, artist_id, release_year, title, tracks_no)
    VALUES ('fb84a7b0-e030-4946-bbeb-c351b2baacb6', 'b9428932-489b-44ae-95b6-99a1af7b46dd', 1973, 'The Dark Side of the Moon', 9);
INSERT INTO albums (album_id, artist_id, release_year, title, tracks_no)
    VALUES ('b6066521-1312-4530-8748-4b6fcd580445', '4368929f-4ea4-4685-b778-c7e7fc83ea05', 1980, 'Back in Black', 10);
INSERT INTO albums (album_id, artist_id, release_year, title, tracks_no)
    VALUES ('9456b892-3074-4b53-8c11-3cc686e84cf0', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 2000, 'The Marshall Mathers LP', 19);
INSERT INTO albums (album_id, artist_id, release_year, title, tracks_no)
    VALUES ('42d65ad9-d961-45ce-b7d9-caa66babec6a', '40e6215d-b5c6-4896-987c-f30f3678f608', 1987, 'Bad', 11);

-- Michael Jackson - Thriller
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('a14eed5c-3fff-4ebd-8edd-59135e31b6db', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 1, 'Wanna Be Startin Somethin', 364);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('4f450073-4d11-4597-aee5-0c0ccb96f89b', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 2, 'Baby Be Mine', 261);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('0503c2c0-41fa-438a-9fbf-a4f3c6afe264', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 3, 'This Girl Is Mine', 223);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('9078b38e-4ee7-433d-8f1b-2fd03bd37531', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 4, 'Thriller', 358);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('21ae0995-5a2b-472f-980b-4c25a7a6c688', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 5, 'Beat It', 258);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('ce7016f5-fe4b-492d-84e8-f19659a3edf5', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 6, 'Billie Jean', 294);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('252ae70a-299b-4e59-a8f1-cad122758c33', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 7, 'Human Nature', 247);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('dac61c8d-0d5d-42d0-ac31-e65d17378c9f', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 8, 'P.Y.T. (Pretty Young Thing)', 238);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('ac9ca96f-c64e-41d4-a94d-1002a6a2ef0e', '6d4f377c-c10f-4d4f-b4ae-96776f0270e1', 9, 'The Lady Is My Life', 299);

-- Pink Floyd - The Dark Side of the Moon
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('927740b6-947c-4c12-94a3-5fc62a73d7f6', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 1, 'Speak to Me/Breathe', 237);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('b1868fb9-1c10-47aa-8dfb-eef58e2e20ef', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 2, 'On the Run', 230);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('120ee3e6-4cea-4d05-a195-05732e6b4950', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 3, 'Time',  409);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('1b3505f8-6db7-42df-99eb-4c7747067bd4', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 4, 'The Great Gig in the Sky', 284);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('9c5ce613-9941-4b7b-b1f1-8984c891fec0', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 5, 'Money', 382);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('be9574a8-e8c7-4345-a75f-ad5349c2e039', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 6, 'Us and Them', 469);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('207101e1-07af-4474-8068-dcc617f4784c', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 7, 'Any Colour You Like', 206);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('c2c0ed2b-1413-4ca5-9f68-9df42a12f351', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 8, 'Brain Damage', 226);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('c3d8d8dd-e835-469c-b983-37c0fd1c9b02', 'fb84a7b0-e030-4946-bbeb-c351b2baacb6', 9, 'Eclipse', 131);

-- AC/DC - Back in Black
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('1cb5eac4-4ca1-4f89-be6e-60fb37e6db5f', 'b6066521-1312-4530-8748-4b6fcd580445', 1, 'Hells Bells', 312);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('82557084-8d45-4d11-9acc-341feb4f4bd8', 'b6066521-1312-4530-8748-4b6fcd580445', 2, 'Shoot to Thrill', 317);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('1af7a08d-3b62-43a8-94db-e84e023c16d7', 'b6066521-1312-4530-8748-4b6fcd580445', 3, 'What Do You Do for Money Honey',  215);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('3f76f081-70f6-4fdd-b643-f06a73d1ba4c', 'b6066521-1312-4530-8748-4b6fcd580445', 4, 'Givin′ the Dog a Bone', 211);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('6c5b8a09-7770-48d9-8bb3-c70299d30912', 'b6066521-1312-4530-8748-4b6fcd580445', 5, 'Let Me Put My Love into You', 255);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('ed5b7a43-144a-490f-99ac-c13c999ed358', 'b6066521-1312-4530-8748-4b6fcd580445', 6, 'Back in Black', 255);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('138f3da3-34d8-44b6-980d-d469ae93b4ef', 'b6066521-1312-4530-8748-4b6fcd580445', 7, 'You Shook Me All Night Long', 210);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('1dd4eb9b-fa66-4912-8951-e6e268933b78', 'b6066521-1312-4530-8748-4b6fcd580445', 8, 'Have a Drink on Me', 238);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('e0f639a8-2a73-4a24-83c2-421f23aebe45', 'b6066521-1312-4530-8748-4b6fcd580445', 9, 'Shake a Leg', 245);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('bef48211-56ce-4e04-b36b-197930404836', 'b6066521-1312-4530-8748-4b6fcd580445', 10, 'Rock and Roll Ain`t Noise Pollution', 256);

-- Eminem - The Marshall Mathers LP
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('5e863768-7bf4-44c9-9493-2cc3d51237e1', '9456b892-3074-4b53-8c11-3cc686e84cf0', 1, 'Public Service Announcement 2000', 25);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('92e44781-13ea-44a4-a0b9-83a9466f9331', '9456b892-3074-4b53-8c11-3cc686e84cf0', 2, 'Kill You', 264);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('a2b3b2db-47f3-45d5-87a7-e524503c52bb', '9456b892-3074-4b53-8c11-3cc686e84cf0', 3, 'Stan', 404);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('817f74bc-ef71-40b3-9d22-52a4d6d2a5c9', '9456b892-3074-4b53-8c11-3cc686e84cf0', 4, 'Paul', 10);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('a0eb2cec-3b41-4e7b-985c-da3b86120db5', '9456b892-3074-4b53-8c11-3cc686e84cf0', 5, 'Who Knew', 227);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('63e9be68-e6d9-47e2-95bc-c1ef277f5072', '9456b892-3074-4b53-8c11-3cc686e84cf0', 6, 'Steve Berman', 53);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('9656fb4c-7a19-4b89-be8c-8bd18c7343df', '9456b892-3074-4b53-8c11-3cc686e84cf0', 7, 'The Way I Am', 290);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('cb678d12-ac2a-41de-90f3-ac8d5976e186', '9456b892-3074-4b53-8c11-3cc686e84cf0', 8, 'The Real Slim Shady', 244);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('5442f2e1-7bc2-42e6-aea7-5ee5013b3613', '9456b892-3074-4b53-8c11-3cc686e84cf0', 9, 'Remember Me?', 218);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('bc3857d7-8de1-4a73-ae73-b3a96194ac5d', '9456b892-3074-4b53-8c11-3cc686e84cf0', 10, 'I`m Back', 310);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('23c91d0b-3b0f-42f3-80f5-45d948cc724b', '9456b892-3074-4b53-8c11-3cc686e84cf0', 11, 'Marshall Mathers', 320);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('580cfb77-b386-4b84-962f-8c4cfe2b7993', '9456b892-3074-4b53-8c11-3cc686e84cf0', 12, 'Ken Kaniff', 61);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('84d0b8e3-d695-4698-9497-709cc5f4e0b7', '9456b892-3074-4b53-8c11-3cc686e84cf0', 13, 'Drug Ballad', 300);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('f4638462-8d2a-4398-aca2-015df84f805a', '9456b892-3074-4b53-8c11-3cc686e84cf0', 14, 'Amityville', 254);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('57e84092-8678-454a-8e33-4c16fbbd3946', '9456b892-3074-4b53-8c11-3cc686e84cf0', 15, 'Bitch Please II', 288);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('173c1e60-1382-40fc-a65f-6ec72995c6a8', '9456b892-3074-4b53-8c11-3cc686e84cf0', 16, 'Kim', 377);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('7da719a4-c0a3-45c3-acf4-f6b7bc213b13', '9456b892-3074-4b53-8c11-3cc686e84cf0', 17, 'Under the Influence', 321);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('9717e431-f3c5-41f3-ac94-5961ef43f1d9', '9456b892-3074-4b53-8c11-3cc686e84cf0', 18, 'Criminal', 315);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('f6bf30e1-cd9c-4824-8a32-267fc13917d1', '9456b892-3074-4b53-8c11-3cc686e84cf0', 19, 'The Kids', 317);

-- Michael Jackson - Bad
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('5e2ddf2c-f321-4c04-8ca9-d2bcae8b5b3f', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 1, 'Bad', 247);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('9e56c6d7-1878-40f8-ac7b-231828cca29d', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 2, 'The Way You Make Me Feel', 299);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('eec84619-bc52-47cd-af4e-2ce2fa59d2b4', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 3, 'Speed Demon', 241);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('cbce2976-3abd-45b1-8b27-9a54a414436f', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 4, 'Liberian Girl', 237);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('887bf3d2-1c08-4539-bed9-6950e21cf4e2', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 5, 'Just Good Friends', 248);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('bb4695a5-21d0-45ee-a893-5e205cb9573a', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 6, 'Another Part of Me', 234);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('b7a7823a-f623-4278-be76-bf5a78cb4910', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 7, 'Man in the Mirror', 319);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('070a05f0-37ac-4c11-b252-d612630f9110', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 8, 'I Just Can`t Stop Loving You', 265);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('68e691a9-a068-44f4-8b58-2d5a3b5a5e0a', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 9, 'Dirty Diana', 292);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('bbb2a486-589d-4f3f-b26d-7370f1d718fd', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 10, 'Smooth Criminal', 259);
INSERT INTO tracks(track_id, album_id, track_no, title, duration)
    VALUES('2243a299-71c0-4b60-b6f3-07a02f8e99ea', '42d65ad9-d961-45ce-b7d9-caa66babec6a', 11, 'Leave Me Alone', 278);


-- !Downs

DROP TABLE artists;
DROP TABLE albums;
DROP TABLE tracks;