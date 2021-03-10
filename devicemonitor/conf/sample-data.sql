-- prepare a list of customers
insert into customer values (1615364362372, 'Fine Food AB');
insert into customer values (1615364438828, 'Glass Maker');
insert into customer values (1615364518724, 'South Wood Manufacture Company');

-- prepare a list of devices
insert into device values (1615364629409, 'Device #1 for inspecting regular food expiration', 1615364362372, 1615364629409, 'ACTIVE');
insert into device values (1615364795663, 'Device #2 for inspecting frozen food expiration', 1615364362372, 1615364795663, 'ACTIVE');
insert into device values (1615364850170, 'Device #1 for inspecting vehicle glass quality at Dept-1', 1615364438828, 1615364850170, 'ACTIVE');
insert into device values (1615364911470, 'Device #2 for inspecting housing glass quality at Dept-1', 1615364438828, 1615364911470, 'ACTIVE');
insert into device values (1615364962871, 'Device #3 for inspecting wearable glass quality at Dept-1', 1615364438828, 1615364962871, 'ACTIVE');
insert into device values (1615365063595, 'Device #1 for inspecting vehicle glass quality at Dept-2', 1615364438828, 1615365063595, 'ACTIVE');
insert into device values (1615365073743, 'Device #2 for inspecting housing glass quality at Dept-2', 1615364438828, 1615365073743, 'ERROR');
insert into device values (1615365083248, 'Device #3 for inspecting wearable glass quality at Dept-2', 1615364438828, 1615364962871, 'ACTIVE');
insert into device values (1615365216906, 'Device #1 for detecting flaws of furniture', 1615364518724, 1615365216906, 'ACTIVE');

-- prepare records of device log
insert into device_log values (1615365439470, 1615364629409, 'ACTIVE', 6, null, 1615365439470);
insert into device_log values (1615365635227, 1615364795663, 'ACTIVE', 12, null, 1615365635227);
insert into device_log values (1615365643132, 1615364850170, 'ACTIVE', 4, null, 1615365643132);
insert into device_log values (1615365650469, 1615364911470, 'ACTIVE', 7, null, 1615365650469);
insert into device_log values (1615365658144, 1615364962871, 'ACTIVE', 8, null, 1615365658144);
insert into device_log values (1615365665392, 1615365063595, 'ACTIVE', 15, null, 1615365665392);
insert into device_log values (1615365671836, 1615365073743, 'ERROR', null, 'Out of memory error', 1615365671836);
insert into device_log values (1615365686677, 1615365083248, 'ACTIVE', 13, null, 1615365686677);
insert into device_log values (1615365693285, 1615365216906, 'ACTIVE', 2, null, 1615365693285);
