from smbus import SMBus

bus = SMBus(0)
address = 7

def send_byte(byte):
	bus.write_byte_data(address, 0, byte)
	return None;

for i in sys.argv:
	send_byte(i)